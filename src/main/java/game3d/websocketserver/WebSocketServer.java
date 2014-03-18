package game3d.websocketserver;

import game3d.socketserver.AbstractServer;

public class WebSocketServer extends AbstractServer {

	private static WebSocketServer that;

	public static WebSocketServer getInstance() {
		if (that == null) {
			synchronized (WebSocketServer.class) {
				if (that == null) {
					that = new WebSocketServer();
				}
			}
		}
		return that;
	}
}
