package io.netty.handler.codec.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.mapping.RequestPackageWrapper;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public class ChannelPackageDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final Logger LOG = Logger.getLogger(ChannelPackageDecoder.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		String message = msg.toString(Charset.forName("UTF-8"));
		LOG.trace("Received message:" + message);

		try {
			JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
			JsonElement commandJE = jsonObject.get("command");
			JsonElement dataJE = jsonObject.get("data");

			if (commandJE == null) {
				throw new IllegalStateException("Field 'command' not found!'");
			}
			if (dataJE == null) {
				throw new IllegalStateException("Field 'data' not found!'");
			}

			String command = commandJE.getAsString();
			String data = dataJE.getAsJsonObject().toString();

			out.add(new RequestPackageWrapper(command, data));
		} catch (Exception e) {
			throw new DecoderException("Not a JSON message format: " + message, e);
		}
	}
}
