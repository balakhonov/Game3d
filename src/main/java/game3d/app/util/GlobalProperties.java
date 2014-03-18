package game3d.app.util;

public class GlobalProperties {
	private static final String PROJECT_PROPERTIES_FILENAME = "util.properties";
	private static PropertiesLoader pl = new PropertiesLoader(PROJECT_PROPERTIES_FILENAME);

	private static final String PROJECT_VERSION = pl.getString("PROJECT_VERSION");
	private static final boolean IS_DEBUG = pl.getBoolean("IS_DEBUG");
	private static final String PROTOCOL = pl.getString("PROTOCOL");
	private static final String SSL_CERTIFICATE_PASSWORD = pl.getString("SSL_CERTIFICATE_PASSWORD");
	private static final String SSL_KEY_STORE_PASSWORD = pl.getString("SSL_KEY_STORE_PASSWORD");
	private static final String SSL_KEY_STORE_PATH = pl.getString("SSL_KEY_STORE_PATH");
	private static final boolean IS_LOCAL_TESTING = pl.getBoolean("IS_LOCAL_TESTING");
	private static final boolean IS_PRODUCTION = pl.getBoolean("IS_PRODUCTION");
	private static final boolean MOBILE_VERSION_SUPPORT = pl.getBoolean("IS_MOBILE_VERSION_SUPPORT");

	public static String getProjectVersion() {
		return PROJECT_VERSION;
	}

	public static boolean isDebug() {
		return IS_DEBUG;
	}

	public static String getProtocol() {
		return PROTOCOL;
	}

	public static String getSslCertificatePassword() {
		return SSL_CERTIFICATE_PASSWORD;
	}

	public static String getSslKeyStorePassword() {
		return SSL_KEY_STORE_PASSWORD;
	}

	public static String getSslKeyStorePath() {
		return SSL_KEY_STORE_PATH;
	}

	public static boolean isLocalTesting() {
		return IS_LOCAL_TESTING;
	}

	public static boolean isProduction() {
		return IS_PRODUCTION;
	}

	public static boolean isMobileVersionSupport() {
		return MOBILE_VERSION_SUPPORT;
	}
}
