package game3d.timeout;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ConnectionManager implements Runnable {

	private Map<String, ConectionTimeoutAction> map = new ConcurrentHashMap<>();

	public void add(String sessionId, ConectionTimeoutAction c) {
		map.put(sessionId, c);
	}

	public void remove(String sessionId) {
		map.remove(sessionId);
	}

	@Override
	public void run() {
		for (ConectionTimeoutAction c : map.values()) {
			c.run();
		}
	}

}
