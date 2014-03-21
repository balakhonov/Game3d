package game3d.websocketserver;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.HOST;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import game3d.Room;
import game3d.RoomFactory;
import game3d.app.controllers.IndexController;
import game3d.mapping.Tank;
import game3d.mapping.User;
import game3d.socketserver.DevicePackageProcessor;
import game3d.socketserver.model.DeviceSocketChannel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.mapping.RequestPackageWrapper;
import io.netty.handler.mapping.ResponsePackageData;
import io.netty.handler.timeout.IdleTimeoutListener;
import io.netty.handler.timeout.auth.AtuhTimeoutListener;
import io.netty.util.CharsetUtil;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Required;


/**
 * Handles handshakes and messages
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> implements
		DeviceSocketChannel, AtuhTimeoutListener, IdleTimeoutListener {
	private static final Logger LOG = Logger.getLogger(WebSocketServerHandler.class);

	private final static ObjectMapper mapper = new ObjectMapper();

	static {
		final int ROOM_ID = 1;
		final Room room = RoomFactory.getRoom(ROOM_ID);

		final Random r = new Random();
		for (int i = 0; i < 10; i++) {
			String sessionId = i + "id";
			int tankType = r.nextInt(2);

			Tank t = new Tank(sessionId, 100);
			t.setpX(-1 * r.nextInt(30));
			t.setpZ(-1 * r.nextInt(30));
			t.setTankType(tankType);

			User user = new User();
			user.setSessionId(sessionId);
			user.setCurrentRoom(ROOM_ID);
			user.setActive(true);
			user.setCurrentTank(t);

			room.addUser(user);
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					for (int i = 0; i < 10; i++) {
						String userId = i + "id";
						User user = room.getUsers().get(userId);

						Tank t = user.getCurrentTank();
						t.setMoveForwardFlag(false);
						t.setMoveBackFlag(false);
						t.setTurnLeftFlag(false);
						t.setTurnRightFlag(false);

						if (t.getpZ() > 100 || t.getpZ() < -100 || t.getpX() > 100
								|| t.getpX() < -100) {
							t.setMoveBackFlag(true);
						} else {
							int act = r.nextInt(3);
							switch (act) {
							case 0:
								t.setMoveForwardFlag(true);
								break;
							case 1:
								t.setMoveForwardFlag(true);
								break;
							case 2:
								t.setTurnLeftFlag(true);
								break;
							case 3:
								t.setTurnRightFlag(true);
								break;
							}
						}
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

	private static final String WEBSOCKET_PATH = "/websocket";

	private WebSocketServerHandshaker handshaker;

	private boolean authorized = false;

	private DevicePackageProcessor packageProcessor;

	private DeviceInfo di;

	public WebSocketServerHandler() {

	}

	@Required
	public void setPackageProcessor(DevicePackageProcessor packageProcessor) {
		this.packageProcessor = packageProcessor;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		LOG.debug("channelRead0:" + ctx + " " + msg);
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		} else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		} else if (msg instanceof RequestPackageWrapper) {
			ResponsePackageData response = packageProcessor.process((RequestPackageWrapper) msg,
					this);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	public static void writeToRoom(Package pack, Room room) {
		Set<Channel> channels = room.getChannels();

		// synchronized (channels) {
		Iterator<Channel> iter = channels.iterator();
		while (iter.hasNext()) {
			channelWrite(iter.next(), pack);
		}
		// }
	}

	public static void channelWrite(Channel channel, Package pack) {
		try {
			String jsonData = mapper.writeValueAsString(pack);
			channel.writeAndFlush(new TextWebSocketFrame(jsonData));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new IllegalStateException(e);
		}
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		// Handle a bad request.
		if (!req.getDecoderResult().isSuccess()) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}

		// Allow only GET methods.
		if (req.getMethod() != GET) {
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
			return;
		}

		String sessionId = req.getUri().substring(1);
		LOG.debug("sessionId: " + sessionId);

		if (IndexController.USERS_MAP.containsKey(sessionId)) {
			User user = IndexController.USERS_MAP.get(sessionId);

			// Handshake
			WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
					getWebSocketLocation(req), null, false);
			handshaker = wsFactory.newHandshaker(req);
			if (handshaker == null) {
				WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx
						.channel());
			} else {
				handshaker.handshake(ctx.channel(), req);
			}

			di = new DeviceInfo();
			di.setSessionId(sessionId);

			// add user to room
			Room room = RoomFactory.getRoom(user.getCurrentRoom());
			room.addUser(user);
			room.addChannel(ctx.channel());
		}
	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// Check for closing frame
		if (frame instanceof CloseWebSocketFrame) {
			// close channel
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());

			// FIXME remove user from room
			User user = IndexController.USERS_MAP.get(di.getSessionId());
			user.getCurrentTank().setConnected(false);

			Room room = RoomFactory.getRoom(user.getCurrentRoom());
			// room.removeUser(user.getSessionId());
			room.removeChannel(ctx.channel());

			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		if (frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
			String text = textFrame.text();

			LOG.trace(String.format("%s received %s", ctx.channel(), text));

			ctx.channel().write(new TextWebSocketFrame(text.toUpperCase()));
		} else {
			throw new UnsupportedOperationException(String.format("%s frame types not supported",
					frame.getClass().getName()));
		}
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req,
			FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	private static String getWebSocketLocation(FullHttpRequest req) {
		return "ws://" + req.headers().get(HOST) + WEBSOCKET_PATH;
	}

	@Override
	public void firstReadTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void readTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void firstWriteTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void writeTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void firstAllTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void allTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public void authTimeout(ChannelHandlerContext ctx) {
	}

	@Override
	public boolean isAuthorized() {
		return authorized;
	}

	@Override
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	@Override
	public DeviceInfo getDeviceInfo() {
		return di;
	}

	@Override
	public void setDeviceInfo(DeviceInfo di) {
		this.di = di;
	}
}
