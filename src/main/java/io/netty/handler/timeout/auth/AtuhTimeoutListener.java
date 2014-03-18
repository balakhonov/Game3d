package io.netty.handler.timeout.auth;

import io.netty.channel.ChannelHandlerContext;

public interface AtuhTimeoutListener {

	public void authTimeout(ChannelHandlerContext ctx);

}
