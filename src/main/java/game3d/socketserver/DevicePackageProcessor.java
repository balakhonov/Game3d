package game3d.socketserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import game3d.model.GameModel;
import game3d.socketserver.model.DeviceRequest;
import game3d.socketserver.model.DeviceSocketChannel;
import io.netty.handler.mapping.RequestPackageWrapper;
import io.netty.handler.mapping.ResponsePackageData;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Method;


public class DevicePackageProcessor {

	private static final Logger LOG = Logger.getLogger(DevicePackageProcessor.class);
	private static final int OTHER = 999;
	private static final String CANT_DESERIALIZE_JSON = "Can't deserialize data('%s'). Data should be instance of '%s'";
	private static final String COMMAND_NOT_FOUND = "No one mapped(%s) command found";
	private static final String REQUEST_DATA_IS_NULL = "RequestData should not be null";


	public ResponsePackageData process(RequestPackageWrapper requestData, DeviceSocketChannel ac)
			throws IOException {
		if (requestData == null) {
			return new ResponsePackageData(OTHER, REQUEST_DATA_IS_NULL);
		}
		if (ac == null) {
			return new ResponsePackageData(OTHER, "AuthenticationProvider == null");
		}

		return execute(requestData, ac);
	}


	private ResponsePackageData execute(RequestPackageWrapper requestData, DeviceSocketChannel ac) {
		String command = requestData.getCommand();
		String data = requestData.getData();

		GameModel dsm = new GameModel(ac);
		for (Method m : dsm.getClass().getMethods()) {
			DeviceRequest dr = m.getAnnotation(DeviceRequest.class);
			if (dr != null) {
				String drCommand = dr.command();
				if (drCommand.equals(command)) {
					return invoke(m, data, dsm);
				}
			}
		}
		return new ResponsePackageData(OTHER, String.format(COMMAND_NOT_FOUND, command));
	}


	private ResponsePackageData invoke(Method m, String data, GameModel dsm) {
		Class<?> dataType = null;

		try {
			if (m.getParameterTypes().length == 0) {
				return (ResponsePackageData) m.invoke(dsm);
			}

			dataType = m.getParameterTypes()[0];
			Object parameter = new Gson().fromJson(data, dataType);
			Object res = m.invoke(dsm, parameter);
			return (ResponsePackageData) res;
		} catch (JsonSyntaxException e) {
			LOG.error(String.format(CANT_DESERIALIZE_JSON, data, dataType), e);
			return new ResponsePackageData(OTHER, String.format(CANT_DESERIALIZE_JSON, data,
					dataType));
		} catch (Exception e) {
			LOG.error(e);
			return new ResponsePackageData(OTHER, e.getMessage());
		}
	}
}
