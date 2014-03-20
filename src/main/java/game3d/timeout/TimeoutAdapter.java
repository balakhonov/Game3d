package game3d.timeout;


public interface TimeoutAdapter {
	public TimeoutHandler getTimeoutHandler();

	public void setTimeoutHandler(TimeoutHandler cth);

	public void onExpired();
}
