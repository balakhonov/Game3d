package game3d.model;

import game3d.Room;
import game3d.RoomFactory;
import game3d.app.controllers.IndexController;
import game3d.mapping.Tank;
import game3d.mapping.User;
import game3d.socketserver.model.DeviceRequest;
import game3d.socketserver.model.DeviceSocketChannel;
import game3d.websocketserver.handler.ObjectHandler;
import io.netty.handler.mapping.ResponsePackageData;

import org.apache.log4j.Logger;


public class GameModel {
	private static final Logger LOG = Logger.getLogger(GameModel.class);

	private Tank tank;
	private Room room;

	public GameModel(DeviceSocketChannel ac) {
		if (ac == null) {
			throw new IllegalArgumentException("ChannelActivity should not be null");
		}
		String sessionId = ac.getDeviceInfo().getSessionId();

		User user = IndexController.USERS_MAP.get(sessionId);

		this.room = RoomFactory.getRoom(user.getCurrentRoom());
		this.tank = room.getTanks().get(sessionId);
	}

	@DeviceRequest(command = "forwardDown")
	public ResponsePackageData forwardDown() {
		tank.setMoveForwardFlag(true);
		return null;
	}

	@DeviceRequest(command = "forwardUp")
	public ResponsePackageData forwardUp() {
		tank.setMoveForwardFlag(false);
		ObjectHandler.updatePosition(room, tank);
		return null;
	}

	@DeviceRequest(command = "backDown")
	public ResponsePackageData backDown() {
		tank.setMoveBackFlag(true);
		return null;
	}

	@DeviceRequest(command = "backUp")
	public ResponsePackageData backUp() {
		tank.setMoveBackFlag(false);
		ObjectHandler.updatePosition(room, tank);
		return null;
	}

	@DeviceRequest(command = "rotateLeftDown")
	public ResponsePackageData rotateLeftDown() {
		tank.setTurnLeftFlag(true);
		return null;
	}

	@DeviceRequest(command = "rotateLeftUp")
	public ResponsePackageData rotateLeftUp() {
		tank.setTurnLeftFlag(false);
		ObjectHandler.updatePosition(room, tank);
		return null;
	}

	@DeviceRequest(command = "rotateRightDown")
	public ResponsePackageData rotateRightDown() {
		tank.setTurnRightFlag(true);
		return null;
	}

	@DeviceRequest(command = "rotateRightUp")
	public ResponsePackageData rotateRightUp() {
		tank.setTurnRightFlag(false);
		ObjectHandler.updatePosition(room, tank);
		return null;
	}
}
