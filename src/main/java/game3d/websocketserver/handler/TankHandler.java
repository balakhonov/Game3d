package game3d.websocketserver.handler;

import game3d.mapping.Tank;
import game3d.websocketserver.Package;
import game3d.websocketserver.WebSocketServerHandler;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TankHandler {

	public static void initAll(Channel channel, Collection<Tank> tank) {
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.channelWrite(channel, new Package("init_all_tanks", tank));
	}


	public static void init(int roomId, Tank tank) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.writeToRoom(new Package("init_new_tank", tank), roomId);
	}


	public static void remove(int roomId, Tank tank) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.writeToRoom(new Package("remove_tank", tank), roomId);
	}


	public static void updatePosition(int roomId, Tank tank) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("userId", tank.getUserId());
		map.put("x", tank.getpX());
		map.put("y", tank.getpY());
		map.put("z", tank.getpZ());

		WebSocketServerHandler.writeToRoom(new Package("update_tank_position", map), roomId);
	}


	public static void updateRotation(int roomId, Tank tank) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("userId", tank.getUserId());
		map.put("x", tank.getrX());
		map.put("y", tank.getrY());
		map.put("z", tank.getrZ());

		WebSocketServerHandler.writeToRoom(new Package("update_tank_rotation", map), roomId);
	}
}
