package game3d.websocketserver.handler;

import game3d.Object3d;
import game3d.websocketserver.Package;
import game3d.websocketserver.WebSocketServerHandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ObjectHandler {

	public static void updatePosition(final int roomId, final Object3d obj) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (obj == null) {
			throw new IllegalArgumentException("Object3d should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id", obj.getId());
		map.put("rx", obj.getrX());
		map.put("ry", obj.getrY());
		map.put("rz", obj.getrZ());
		map.put("px", obj.getpX());
		map.put("py", obj.getpY());
		map.put("pz", obj.getpZ());
//		System.out.println(map);

		WebSocketServerHandler.writeToRoom(new Package("update_tank_position", map), roomId);
	}


	public static void updateRotation(int roomId, Object3d obj) {
		if (roomId < 1) {
			throw new IllegalArgumentException("Room ID(" + roomId + ") should not be < 1");
		}
		if (obj == null) {
			throw new IllegalArgumentException("Object3d should not be null");
		}

		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("id", obj.getId());
		map.put("rx", obj.getrX());
		map.put("ry", obj.getrY());
		map.put("rz", obj.getrZ());
		map.put("px", obj.getpX());
		map.put("py", obj.getpY());
		map.put("pz", obj.getpZ());

		WebSocketServerHandler.writeToRoom(new Package("update_tank_rotation", map), roomId);
	}
}
