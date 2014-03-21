package game3d.timeout;

import game3d.Room;
import game3d.effect.DelayAction;

import java.util.Date;


public abstract class ConectionTimeoutAction extends DelayAction {

	/**
	 * Delay between timeout checking in seconds
	 */
	private static final int DEFAULT_CHECK_DELAY = 1;

	protected final Room room;
	protected final Connection connection;
	protected final int timeout;

	public ConectionTimeoutAction(Connection connection, Room room, int timeout) {
		super(DEFAULT_CHECK_DELAY);

		if (connection == null) {
			throw new IllegalArgumentException("Connection should not be null");
		}
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (timeout < 0) {
			throw new IllegalArgumentException("Timeout should not be < 0");
		}

		this.connection = connection;
		this.room = room;
		this.timeout = timeout;

		connection.setConnected(true);
	}

	public abstract void onTimeout();

	@Override
	public final void action() {
		long currentTime = new Date().getTime();
		if (connection.isConnected()) {
			setEndDate(currentTime);
		} else {
			if (currentTime - getEndDate() >= timeout) {
				onTimeout();
				setStopped(true);
			}
		}
	}
}
