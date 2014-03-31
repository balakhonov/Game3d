package game3d.app.controllers;

import com.google.gson.Gson;

import game3d.Room;
import game3d.RoomFactory;
import game3d.Tower;
import game3d.Weapon;
import game3d.app.util.GlobalProperties;
import game3d.mapping.AbstractTank;
import game3d.mapping.User;
import game3d.motion.Engine;
import game3d.motion.Suspension;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.security.validator.ValidatorException;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Controller
public class IndexController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(IndexController.class);

	/**
	 * Contains names of tanks objects
	 */
	private static final List<String> TANK_OBJ_SET = new ArrayList<>();

	static {
		TANK_OBJ_SET.add("tank-T-34-85");
		TANK_OBJ_SET.add("tank-Tiger");
	}

	/**
	 * key -sessionId
	 */
	public static final Map<String, User> USERS_MAP = new ConcurrentHashMap<>();
	private static int userIdGenerator = 0;

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request) {
		try {
			if (USERS_MAP.containsKey(request.getSession().getId())) {
				LOG.info("User found: " + request.getSession().getId());
				LOG.info("USERS_MAP: " + USERS_MAP);
				return account(request.getSession().getId());
			} else {
				LOG.info("User not found: " + request.getSession().getId());
				LOG.info("USERS_MAP: " + USERS_MAP);
				return new ModelAndView("panel/login");
			}
		} catch (Exception e) {
			LOG.error(e);
			return getErrorPageView();
		}
	}

	private ModelAndView account(String sessionId) {
		User user = USERS_MAP.get(sessionId);

		Map<String, Object> mapData = new HashMap<>();
		mapData.put("SESSION_ID", sessionId);
		mapData.put("USER", new Gson().toJson(user));
		mapData.put("PROTOCOL", GlobalProperties.getProtocol());
		mapData.put("PROJECT_VERSION", GlobalProperties.getProjectVersion());
		mapData.put("TANK_OBJ_SET", new Gson().toJson(TANK_OBJ_SET));

		return new ModelAndView("panel/index", mapData);
	}

	@RequestMapping(value = {"/"}, method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> logIn(HttpServletRequest request) {
		Map<String, Object> mapData = new HashMap<>();
		try {
			String sessionId = request.getSession().getId();
			String userName = request.getParameter("userName");

			if (userName == null || userName.isEmpty()) {
				throw new ValidatorException("User name should be empty");
			}

			User user = new User();
			user.setId(++userIdGenerator);
			user.setName(userName);
			user.setActive(true);
			user.setSessionId(sessionId);

			USERS_MAP.put(sessionId, user);

			mapData.put("RESULT_CODE", 0);
		} catch (ValidatorException e) {
			mapData.put("RESULT_CODE", 1);
			mapData.put("MESSAGE", e.getMessage());
		} catch (Exception e) {
			LOG.error(e);
			mapData.put("RESULT_CODE", 999);
			mapData.put("MESSAGE", e.getMessage());
		}

		return mapData;
	}

	@RequestMapping(value = {"/room"}, method = RequestMethod.POST)
	public ModelAndView enterRoom(@RequestParam(required = true, value = "roomId") int roomId,
								  @RequestParam(required = true, value = "tankType") int tankType,
								  HttpServletRequest request) {
		LOG.info("tankType: " + tankType);
		LOG.info("roomId: " + roomId);
		String sessionId = request.getSession().getId();

		AbstractTank tank = new AbstractTank(sessionId, 1000, new Suspension(), new Engine(),
				new Weapon(), new Tower());
		tank.setTankType(tankType);

		User user = USERS_MAP.get(sessionId);
		if (user == null) {
			return new ModelAndView("panel/login");
		}

		user.setCurrentRoom(roomId);
		user.setCurrentTankType(tankType);
		user.setCurrentTank(tank);

		Room room = RoomFactory.getRoom(user.getCurrentRoom());
		room.removeUser(user.getSessionId());

		return enterRoom(user);
	}

	@RequestMapping(value = {"/room"}, method = RequestMethod.GET)
	public ModelAndView enterRoom(HttpServletRequest request) {
		User user = USERS_MAP.get(request.getSession().getId());
		if (user == null) {
			return new ModelAndView("panel/login");
		}

		return enterRoom(user);
	}

	private ModelAndView enterRoom(User user) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("USER", new Gson().toJson(user));
		mapData.put("SESSION_ID", user.getSessionId());
		mapData.put("TANK_TYPE", user.getCurrentTankType());
		mapData.put("PROTOCOL", GlobalProperties.getProtocol());
		mapData.put("PROJECT_VERSION", GlobalProperties.getProjectVersion());
		mapData.put("TANK_OBJ_SET", new Gson().toJson(TANK_OBJ_SET));

		return new ModelAndView("panel/room", mapData);
	}

}
