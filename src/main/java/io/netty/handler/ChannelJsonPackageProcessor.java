package io.netty.handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import game3d.socketserver.model.DeviceSocketChannel;
import io.netty.annotation.Command;
import io.netty.annotation.SocketController;
import io.netty.handler.mapping.RequestPackageWrapper;
import io.netty.handler.mapping.ResponsePackageData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ChannelJsonPackageProcessor {
	private static final Logger LOG = Logger.getLogger(ChannelJsonPackageProcessor.class);
	private static final int OTHER = 999;

	private static final String PACKAGE_VERSION_PROP_NOT_FOUND = "Property 'serialVersionUID' in data('%s') not found. Request data should extends VersionControl class";
	private static final String CANT_DESEREALIZE_JSON = "Can't deserealize data('%s'). Data should be instance of '%s'";
	private static final String COMMAND_NOT_FOUND = "No one mapped(%s) command found";

	private static String controllersDirectory = "softserve.netty.controller";

	private static final Map<String, InstanceMethod> MAPPED_METHODS = new HashMap<String, InstanceMethod>();

	/**
	 * 
	 */
	private static class InstanceMethod {
		protected Method method;
		protected Object instance;

		public InstanceMethod(Method method, Object instance) {
			this.method = method;
			this.instance = instance;
		}
	}

	/**
	 * 
	 * @param controllersDirectory
	 */
	public static void setControllersDirectory(String controllersDirectory) {
		ChannelJsonPackageProcessor.controllersDirectory = controllersDirectory;
	}

	/**
	 * Scan 'softserve.netty.controller' directory to find SocketController
	 * classes
	 */
	public static void init() {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
		scanner.addIncludeFilter(new AnnotationTypeFilter(SocketController.class));

		for (BeanDefinition bd : scanner.findCandidateComponents(controllersDirectory)) {
			try {
				Class<?> clazz = Class.forName(bd.getBeanClassName());
				Object instance = clazz.newInstance();

				LOG.debug("Socket controller found: " + clazz.getName());

				for (Method m : clazz.getMethods()) {
					Command annotation = (Command) m.getAnnotation(Command.class);
					if (annotation != null) {
						String annotationCommand = annotation.command();
						if (MAPPED_METHODS.containsKey(annotationCommand)) {
							throw new IllegalStateException("Duplicates found in " + clazz.getName() + ", command "
									+ annotationCommand + " alredy mapped.");
						} else {
							LOG.debug("Mapping method: '" + m.getName() + "' to '" + annotationCommand + "'");
							MAPPED_METHODS.put(annotationCommand, new InstanceMethod(m, instance));
						}
					}
				}
			} catch (InstantiationException e) {
				throw new IllegalStateException(e);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			} catch (ClassNotFoundException e) {
				throw new IllegalStateException(e);
			}
		}

		if (MAPPED_METHODS.isEmpty()) {
			LOG.warn("No one socket controller found in '" + controllersDirectory + "'");
		}
	}

	/**
	 * 
	 * @param rpw
	 * @param ac
	 * @return
	 */
	public static ResponsePackageData process(RequestPackageWrapper rpw, DeviceSocketChannel ac) {
		if (rpw == null) {
			throw new IllegalArgumentException("RequestPackageWrapper should not be null");
		}
		if (ac == null) {
			throw new IllegalArgumentException("AuthoeizedChannel should not be null");
		}

		String command = rpw.getCommand();
		String data = rpw.getData();

		InstanceMethod instanceMethod = MAPPED_METHODS.get(command);
		if (instanceMethod == null) {
			return new ResponsePackageData(OTHER, String.format(COMMAND_NOT_FOUND, command));
		} else {
			return invoke(instanceMethod.method, data, instanceMethod.instance, ac);
		}
	}

	/**
	 * 
	 * @param method
	 * @param data
	 * @param instance
	 * @param ac
	 * @return
	 */
	private static ResponsePackageData invoke(Method method, String data, Object instance, DeviceSocketChannel ac) {
		Class<?> dataType = method.getParameterTypes()[0];

		try {
			JsonElement je = new JsonParser().parse(data).getAsJsonObject().get("serialVersionUID");
			if (je == null || je.isJsonNull()) {
				LOG.error(String.format(PACKAGE_VERSION_PROP_NOT_FOUND, data));
				return new ResponsePackageData(OTHER, String.format(PACKAGE_VERSION_PROP_NOT_FOUND, data));
			}

			if (method.getParameterTypes().length == 0) {
				return (ResponsePackageData) method.invoke(instance);
			}

			Object parameter = new Gson().fromJson(data, dataType);
			Object res = method.invoke(instance, parameter, ac);
			return (ResponsePackageData) res;
		} catch (JsonSyntaxException e) {
			LOG.error(String.format(CANT_DESEREALIZE_JSON, data, dataType), e);
			return new ResponsePackageData(OTHER, String.format(CANT_DESEREALIZE_JSON, data, dataType));
		} catch (Exception e) {
			LOG.error(e);
			return new ResponsePackageData(OTHER, e.getMessage());
		}
	}
}
