package game3d.timeout;

import io.netty.util.internal.ConcurrentSet;

import java.util.Set;


public class ConnectionManager implements Runnable {

	private Set<ConectionTimeoutAction> set = new ConcurrentSet<>();

	public void add(ConectionTimeoutAction c) {
		set.add(c);
	}

	public void remove(Connection c) {
		set.remove(c);
	}

	@Override
	public void run() {
		for (ConectionTimeoutAction c : set) {
			c.run();
		}
	}

}
