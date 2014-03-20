package game3d;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RoomFactory {

	private static final Map<Integer, RoomImpl> ROOMS = new ConcurrentHashMap<>();
	static {
		ROOMS.put(1, new RoomImpl());
	}

	public static RoomImpl getRoom(int id) {
		return ROOMS.get(id);
	}
}
