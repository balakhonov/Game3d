package io.netty.handler.codec.json;

import com.google.gson.Gson;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.mapping.VersionControl;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

public class ChannelPackageEncoder extends MessageToMessageEncoder<VersionControl> {
	protected static final Logger LOG = Logger.getLogger(ChannelPackageEncoder.class);
	private static final String LINE_END = "\n\r";

	public static byte[] integerToByteArray(int value) {
		byte[] packageLengthBytes = ByteBuffer.allocate(4).putInt(value).array();
		return packageLengthBytes;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, VersionControl msg, List<Object> out) throws Exception {
		String message = new Gson().toJson(msg) + LINE_END;
		LOG.trace("Package data string: " + message);
		byte[] messageBytes = message.getBytes("UTF-8");
		int packageLength = messageBytes.length;
		LOG.trace("Package length: " + packageLength);

		// get package length
		byte[] packageLengthBytes = integerToByteArray(packageLength);
		LOG.trace("Package length array: " + Arrays.toString(packageLengthBytes));

		// merge package length with package data
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(packageLengthBytes);
		outputStream.write(messageBytes);

		byte[] packageArray = outputStream.toByteArray();
		LOG.trace("Total Package length: " + packageArray.length);
		LOG.trace("Package data: " + Arrays.toString(packageArray));

		out.add(Unpooled.wrappedBuffer(packageArray));
	}
}
