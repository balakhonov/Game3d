package game3d.websocketserver.bean;

import game3d.socketserver.AbstractServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.apache.log4j.Logger;

public class ServerBean {
	private static final Logger LOG = Logger.getLogger(ServerBean.class);
	
	private int port = 9876;
	private int bossGroupThreadSize = 4;
	private int workGroupThreadSize = 4;
	private ChannelInitializer<SocketChannel> channelInitializer;
	
	private AbstractServer server = new AbstractServer();

	public boolean startServer() {
		LOG.debug("ServerBean startServer");
		server.init(port, bossGroupThreadSize, workGroupThreadSize, channelInitializer);
		return server.startServer();
	}

	public boolean stopServer() {
		LOG.debug("ServerBean stopServer");
		return server.stopServer();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossGroupThreadSize() {
		return bossGroupThreadSize;
	}

	public void setBossGroupThreadSize(int bossGroupThreadSize) {
		this.bossGroupThreadSize = bossGroupThreadSize;
	}

	public int getWorkGroupThreadSize() {
		return workGroupThreadSize;
	}

	public void setWorkGroupThreadSize(int workGroupThreadSize) {
		this.workGroupThreadSize = workGroupThreadSize;
	}

	public ChannelInitializer<SocketChannel> getChannelInitializer() {
		return channelInitializer;
	}

	public void setChannelInitializer(ChannelInitializer<SocketChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

	public boolean isStarted() {
		return server.isStarted();
	}
}
