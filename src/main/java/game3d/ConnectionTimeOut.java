package game3d;

public interface ConnectionTimeOut {
	public ConnectionTimeoutHandler getConnectionTimeoutHandler();

	public void setConnectionTimeoutHandler(ConnectionTimeoutHandler cth);

	public void onConnectionTimeout();
}
