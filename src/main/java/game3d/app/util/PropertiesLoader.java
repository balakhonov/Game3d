package game3d.app.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	private static final Logger LOG = Logger.getLogger(PropertiesLoader.class);
	private Properties prop = new Properties();

	public PropertiesLoader(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("Config file name should not be null or empty");
		}

		InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName);
		if (in == null) {
			throw new IllegalArgumentException("File('" + fileName + "') not found in classpath");
		}

		try {
			prop.load(in);
		} catch (IOException e) {
			throw new IllegalStateException("Can't load project properties", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				LOG.error(e);
			}
		}
	}

	private String getProperty(String propName) {
		if (propName == null || propName.isEmpty()) {
			throw new IllegalArgumentException("Property name should not be null or empty");
		}

		try {
			String value = prop.getProperty(propName);
			if (value == null) {
				throw new IllegalStateException("Property '" + propName + "' not found");
			}

			return value;
		} catch (NumberFormatException e) {
			throw new IllegalStateException("Can't convert property '" + propName + "'");
		}
	}

	public boolean getBoolean(String propName) {
		String value = getProperty(propName);
		return Boolean.parseBoolean(value);
	}

	public int getInt(String propName) {
		String value = getProperty(propName);
		return Integer.parseInt(value);
	}

	public long getLong(String propName) {
		String value = getProperty(propName);
		return Long.parseLong(value);
	}

	public String getString(String propName) {
		return getProperty(propName);
	}
}