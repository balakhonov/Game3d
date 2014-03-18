package game3d.socketserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class AbstractServer implements Runnable {
	private static final Logger LOG = Logger.getLogger(AbstractServer.class);
	private int port;
	private int bossGroupThreadSize;
	private int workGroupThreadSize;
	private ChannelHandler channelHandler;

	private boolean started = false;
	private boolean initialized = false;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;


	/**
	 * @param port
	 * @param bossGroupThreadSize
	 * @param workGroupThreadSize
	 * @param channelHandler
	 */
	public synchronized void init(int port, int bossGroupThreadSize, int workGroupThreadSize,
								  ChannelHandler channelHandler) {
		// TODO validate
		this.port = port;
		this.bossGroupThreadSize = bossGroupThreadSize;
		this.workGroupThreadSize = workGroupThreadSize;
		this.channelHandler = channelHandler;
		this.initialized = true;
	}

	/**
	 * @return
	 */
	public synchronized boolean startServer() {
		if (!initialized) {
			LOG.warn("Socket server not initialized");
			return false;
		}

		if (started) {
			LOG.warn("Socket server already started at port " + this.port);
			return false;
		} else {
			LOG.info("Starting socket server");

			Thread workThread = new Thread(this, "Socket server");
			workThread.start();
			started = true;
			return true;
		}
	}

	/**
	 * @return
	 */
	public synchronized boolean stopServer() {
		if (!initialized) {
			LOG.warn("Socket server not initialized");
			return false;
		}

		if (started) {
			LOG.info("Stopping socket server");

			started = false;
			if (bossGroup != null && workerGroup != null) {
				bossGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}
			return true;
		} else {
			LOG.warn("Socket server already stopped");
			return false;
		}
	}

	public boolean isStarted() {
		return started;
	}

	/**
	 *
	 */
	@Override
	public void run() {
		if (!initialized) {
			LOG.warn("Socket server not initialized");
			return;
		}

		try {
			bossGroup = new NioEventLoopGroup(bossGroupThreadSize);
			workerGroup = new NioEventLoopGroup(workGroupThreadSize);

			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(channelHandler);

			LOG.info("Starting Socket Server at port " + port);
			Channel channel = b.bind(port).sync().channel();
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			LOG.error(e);
		} finally {
			// stopServer();
		}
	}
}
