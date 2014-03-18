package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;

public interface IdleTimeoutListener {

	public void firstReadTimeout(ChannelHandlerContext ctx);

	public void readTimeout(ChannelHandlerContext ctx);

	public void firstWriteTimeout(ChannelHandlerContext ctx);

	public void writeTimeout(ChannelHandlerContext ctx);

	public void firstAllTimeout(ChannelHandlerContext ctx);

	public void allTimeout(ChannelHandlerContext ctx);
}
