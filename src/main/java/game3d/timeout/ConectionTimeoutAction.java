package game3d.timeout;

import java.util.Date;

import game3d.effect.DelayAction;


public class ConectionTimeoutAction extends DelayAction {
	private Connection connection;
	private int timeout;

	public ConectionTimeoutAction(Connection connection, int timeout) {
		this(connection, timeout, 1000);
	}

	public ConectionTimeoutAction(Connection connection, int timeout, int period) {
		super(period);

		if (connection == null) {
			throw new IllegalArgumentException("connection should not be null");
		}

		this.connection = connection;
		this.timeout = timeout;
	}

	@Override
	public void action() {
		long currentTime = new Date().getTime();
		if (connection.isConnected()) {
			setEndDate(currentTime);
		} else {
			if (currentTime - getEndDate() >= timeout) {
				System.err.println("timeout");
			}
		}
	}

}
