package game3d.timeout;

import java.util.Date;


public class TimeoutHandlerImpl implements TimeoutHandler {

	private boolean active;
	private boolean disconnected;

	private long timeout;
	private long lastActive;
	private TimeoutAdapter ct;

	public TimeoutHandlerImpl(TimeoutAdapter ct) {
		this(DEFAULT_CONNECTION_TIMEOUT, ct);
	}

	public TimeoutHandlerImpl(long timeout, TimeoutAdapter ct) {
		this.timeout = timeout;
		this.ct = ct;
		this.active = true;
		this.lastActive = new Date().getTime();
	}

	public void checkTimeout() {
		if (!disconnected) {
			if (!active) {
				long currentTime = new Date().getTime();
				if (currentTime - lastActive >= timeout) {
					ct.onExpired();
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
	}

	public void activate() {
		this.active = true;
		this.disconnected = false;
	}
}
