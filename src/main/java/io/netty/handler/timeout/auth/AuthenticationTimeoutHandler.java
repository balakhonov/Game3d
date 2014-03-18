package io.netty.handler.timeout.auth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationTimeoutHandler extends ReadTimeoutHandler {
	private List<AtuhTimeoutListener> listeners = new ArrayList<AtuhTimeoutListener>();

	public AuthenticationTimeoutHandler(int timeoutSeconds) {
		super(timeoutSeconds);
	}

	public void addListener(AtuhTimeoutListener listener) {
		listeners.add(listener);
	}

	@Override
	protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
		for (AtuhTimeoutListener atl : listeners) {
				atl.authTimeout(ctx);
		}
		super.handlerRemoved(ctx);
	}
}