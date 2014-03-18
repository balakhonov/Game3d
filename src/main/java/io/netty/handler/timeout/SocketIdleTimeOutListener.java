package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;

public interface SocketIdleTimeOutListener {

	public void channelFirstReadTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);

	public void channelReadTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);

	public void channelFirstWriteTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);

	public void channelWriteTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);

	public void channelFirstAllTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);

	public void channelAllTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt);
}
