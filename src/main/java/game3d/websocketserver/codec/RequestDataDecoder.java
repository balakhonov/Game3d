package game3d.websocketserver.codec;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.mapping.RequestPackageWrapper;

import java.util.List;


public class RequestDataDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

	@Override
	protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out)
			throws Exception {

		String message = msg.text();
		if ("\r".endsWith(message)) {
			out.add(new PingWebSocketFrame());
			return;
		}

		try {
			JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
			String command = jsonObject.get("type").getAsString();
			String data = jsonObject.get("data").getAsJsonObject().toString();
			RequestPackageWrapper requestData = new RequestPackageWrapper(command, data);
			out.add(requestData);
		} catch (Exception e) {
			throw new Exception("Can't parse message: " + message, e);
		}
	}
}