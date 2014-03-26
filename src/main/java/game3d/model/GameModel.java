package game3d.model;

import game3d.Room;
import game3d.RoomFactory;
import game3d.app.controllers.IndexController;
import game3d.mapping.AbstractTank;
import game3d.mapping.User;
import game3d.motion.MotionController;
import game3d.socketserver.model.DeviceRequest;
import game3d.socketserver.model.DeviceSocketChannel;
import game3d.websocketserver.handler.ObjectHandler;
import game3d.websocketserver.handler.TankHandler;

import org.apache.log4j.Logger;


public class GameModel {
	private static final Logger LOG = Logger.getLogger(GameModel.class);

	private MotionController motionController;
	private AbstractTank tank;
	private Room room;

	public GameModel(DeviceSocketChannel ac) {
		if (ac == null) {
			throw new IllegalArgumentException("ChannelActivity should not be null");
		}
		String sessionId = ac.getDeviceInfo().getSessionId();

		User user = IndexController.USERS_MAP.get(sessionId);

		this.room = RoomFactory.getRoom(user.getCurrentRoom());
		this.tank = room.getTanks().get(sessionId);
		this.motionController = room.getMotionController(sessionId);
	}

	@DeviceRequest(command = "forwardDown")
	public void forwardDown() {
		motionController.setMoveForwardFlag(true);
	}

	@DeviceRequest(command = "forwardUp")
	public void forwardUp() {
		motionController.setMoveForwardFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "backDown")
	public void backDown() {
		motionController.setMoveBackFlag(true);
	}

	@DeviceRequest(command = "backUp")
	public void backUp() {
		motionController.setMoveBackFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "rotateLeftDown")
	public void rotateLeftDown() {
		motionController.setTurnLeftFlag(true);
	}

	@DeviceRequest(command = "rotateLeftUp")
	public void rotateLeftUp() {
		motionController.setTurnLeftFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "rotateRightDown")
	public void rotateRightDown() {
		motionController.setTurnRightFlag(true);
	}

	@DeviceRequest(command = "rotateRightUp")
	public void rotateRightUp() {
		motionController.setTurnRightFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "rotateTowerLeftDown")
	public void rotateTowerLeftDown() {
		motionController.setTurnTowerLeftFlag(true);
	}

	@DeviceRequest(command = "rotateTowerLeftUp")
	public void rotateTowerLeftUp() {
		motionController.setTurnTowerLeftFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "rotateTowerRightDown")
	public void rotateTowerRightDown() {
		motionController.setTurnTowerRightFlag(true);
	}

	@DeviceRequest(command = "rotateTowerRightUp")
	public void rotateTowerRightUp() {
		motionController.setTurnTowerRightFlag(false);
		ObjectHandler.updatePosition(room, tank);
	}

	@DeviceRequest(command = "fire")
	public void fire(String targetSessionId) {
		double dmg = tank.getWeapon().getDamage();
		
		AbstractTank target = room.getTanks().get(targetSessionId);
		target.setHealth(target.getHealth() - dmg);

		TankHandler.wounded(room, target, dmg);
		if (target.getHealth() < 0) {
			TankHandler.destroyed(room, target);
		}
	}
}
