package io.netty.handler.mapping;

public abstract class VersionControl {
	public static final int DEFAULT_VERSION = 0;
	public int serialVersionUID = DEFAULT_VERSION;

	public int getVersion() {
		return DEFAULT_VERSION;
	}

	public boolean haveSameVersion() {
		return serialVersionUID == getVersion();
	}
}
