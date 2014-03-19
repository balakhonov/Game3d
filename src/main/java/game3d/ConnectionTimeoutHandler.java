package game3d;

import java.util.Date;


public class ConnectionTimeoutHandler {
	private boolean active = true;
	private boolean disconnected = false;

	private long timeout = 5000;
	private long lastActive;
	private ConnectionTimeOut ct;

	public ConnectionTimeoutHandler(ConnectionTimeOut ct) {
		this.ct = ct;
		this.lastActive = new Date().getTime();
	}

	public void checkTimeout() {
		if (!disconnected) {
			if (!active) {
				long currentTime = new Date().getTime();
				if (currentTime - lastActive >= timeout) {
					ct.onConnectionTimeout();
					disconnected = true;
				}
			} else {
				lastActive = new Date().getTime();
			}
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		this.disconnected = false;
	}
}
