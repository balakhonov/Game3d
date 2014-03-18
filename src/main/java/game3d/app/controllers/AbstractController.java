package game3d.app.controllers;

import com.google.gson.Gson;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractController {
	protected static Gson GSON = new Gson();

	/**
	 * @return User IP address
	 */
	protected static String getRemoteAddress() {
		RequestAttributes ra = RequestContextHolder.currentRequestAttributes();
		if (ra instanceof ServletRequestAttributes) {
			return ((ServletRequestAttributes) ra).getRequest().getRemoteAddr();
		}
		return "none";
	}

	public static ModelAndView getErrorPageView() {
		return new ModelAndView("panel/error-page");
	}
}