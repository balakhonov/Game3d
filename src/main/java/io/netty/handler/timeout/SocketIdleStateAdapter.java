package io.netty.handler.timeout;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class SocketIdleStateAdapter extends IdleStateHandler {

	private List<IdleTimeoutListener> listeners = new ArrayList<IdleTimeoutListener>();

	public SocketIdleStateAdapter(int readerIdleTimeSeconds, int writerIdleTimeSeconds, int allIdleTimeSeconds) {
		super(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds);
	}

	public void addListener(IdleTimeoutListener listener) {
		listeners.add(listener);
	}

	@Override
	protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
		if (evt.isFirst()) {
			if (evt.state() == IdleState.READER_IDLE) {
				channelFirstReadTimeOut(ctx, evt);
			} else if (evt.state() == IdleState.WRITER_IDLE) {
				channelFirstWriteTimeOut(ctx, evt);
			} else if (evt.state() == IdleState.ALL_IDLE) {
				channelFirstAllTimeOut(ctx, evt);
			}
		} else {
			if (evt.state() == IdleState.READER_IDLE) {
				channelReadTimeOut(ctx, evt);
			} else if (evt.state() == IdleState.WRITER_IDLE) {
				channelWriteTimeOut(ctx, evt);
			} else if (evt.state() == IdleState.ALL_IDLE) {
				channelAllTimeOut(ctx, evt);
			}
		}
	}

	private void channelFirstReadTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.firstReadTimeout(ctx);
		}
	}

	private void channelReadTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.readTimeout(ctx);
		}
	}

	private void channelFirstWriteTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.firstWriteTimeout(ctx);
		}
	}

	private void channelWriteTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.writeTimeout(ctx);
		}
	}

	private void channelFirstAllTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.firstAllTimeout(ctx);
		}
	}

	private void channelAllTimeOut(ChannelHandlerContext ctx, IdleStateEvent evt) {
		for (IdleTimeoutListener itl : listeners) {
			itl.allTimeout(ctx);
		}
	}
}
