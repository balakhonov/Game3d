package game3d.app.controllers;

import game3d.app.util.GlobalProperties;
import game3d.mapping.Tank;
import game3d.websocketserver.handler.TankHandler;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Controller
public class IndexController extends AbstractController {
	private static final Logger LOG = Logger.getLogger(IndexController.class);

	public IndexController() {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				Random r = new Random();
				while (true) {
					Tank tank = new Tank(1 + "", 1000);
					tank.setpX(r.nextFloat() * 50);
					tank.setpZ(r.nextFloat() * 50);

					TankHandler.updatePosition(1, tank);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		// t.start();
	}


	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView getPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("SESSION_ID", request.getSession().getId());
			mapData.put("PROTOCOL", GlobalProperties.getProtocol());
			mapData.put("PROJECT_VERSION", GlobalProperties.getProjectVersion());

			return new ModelAndView("panel/index", mapData);
		} catch (Exception e) {
			LOG.error(e);
			return getErrorPageView();
		}
	}


	@RequestMapping(value = {"/room-{num}"}, method = RequestMethod.GET)
	public ModelAndView getPage(@PathVariable(value = "num") String num, HttpServletRequest request) {
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("SESSION_ID", request.getSession().getId());
		mapData.put("PROTOCOL", GlobalProperties.getProtocol());
		mapData.put("PROJECT_VERSION", GlobalProperties.getProjectVersion());

		return new ModelAndView("panel/room", mapData);
	}


}
