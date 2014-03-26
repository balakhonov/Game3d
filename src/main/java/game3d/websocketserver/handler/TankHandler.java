package game3d.websocketserver.handler;

import game3d.Room;
import game3d.mapping.AbstractTank;
import game3d.websocketserver.Package;
import game3d.websocketserver.WebSocketServerHandler;
import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class TankHandler {

	public static void initAll(Channel channel, Collection<AbstractTank> tank) {
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.channelWrite(channel, new Package("init_all_tanks", tank));
	}

	public static void init(Room room, AbstractTank tank) {
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.writeToRoom(new Package("init_new_tank", tank), room);
	}

	public static void remove(Room room, AbstractTank tank) {
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		WebSocketServerHandler.writeToRoom(new Package("remove_tank", tank), room);
	}

	public static void updatePosition(Room room, AbstractTank tank) {
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("userId", tank.getSessionId());
		map.put("x", tank.getpX());
		map.put("y", tank.getpY());
		map.put("z", tank.getpZ());

		WebSocketServerHandler.writeToRoom(new Package("update_tank_position", map), room);
	}

	public static void updateTowerRotation(Room room, AbstractTank tank) {
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}
		if (!tank.hasTower()) {
			return;
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id", tank.getId());
		map.put("ry", tank.getTower().getrY());

		WebSocketServerHandler.writeToRoom(new Package("on_tower_rotate", map), room);
	}

	public static void updateRotation(Room room, AbstractTank tank) {
		if (room == null) {
			throw new IllegalArgumentException("Room should not be null");
		}
		if (tank == null) {
			throw new IllegalArgumentException("Tank should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("userId", tank.getSessionId());
		map.put("x", tank.getrX());
		map.put("y", tank.getrY());
		map.put("z", tank.getrZ());

		WebSocketServerHandler.writeToRoom(new Package("update_tank_rotation", map), room);
	}
}
