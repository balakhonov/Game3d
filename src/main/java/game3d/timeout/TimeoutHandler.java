package game3d.timeout;

public interface TimeoutHandler {
	final long DEFAULT_CONNECTION_TIMEOUT = 5000;

	public void checkTimeout();

	public boolean isActive();

	public void setActive(boolean active);

	public void activate();
}
