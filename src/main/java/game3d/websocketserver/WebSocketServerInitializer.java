package game3d.websocketserver;

import game3d.websocketserver.codec.RequestDataDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


/**
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
	private static final Logger LOG = Logger.getLogger(WebSocketServerInitializer.class);

	@Autowired(required = true)
	private ApplicationContext appContext;

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		LOG.debug("Init websocket channel1:" + ch);

		// main channel handler
		WebSocketServerHandler wsch = (WebSocketServerHandler) appContext
				.getBean("socketChannelHandler");
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("codec-http", new HttpServerCodec());
		pipeline.addLast("decoder", new RequestDataDecoder());
		pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
		pipeline.addLast("handler", wsch);
	}

}
