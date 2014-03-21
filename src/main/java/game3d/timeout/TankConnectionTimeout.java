package game3d.timeout;

import game3d.Room;
import game3d.mapping.Tank;
import game3d.websocketserver.handler.TankHandler;

public class TankConnectionTimeout extends ConectionTimeoutAction{

	public TankConnectionTimeout(Connection connection, Room room, int timeout) {
		super(connection, room, timeout);
	}

	@Override
	public void onTimeout() {
		Tank tank = (Tank) connection;

		room.removeUser(tank.getSessionId());
		
		TankHandler.remove(room, tank);
	}

}
