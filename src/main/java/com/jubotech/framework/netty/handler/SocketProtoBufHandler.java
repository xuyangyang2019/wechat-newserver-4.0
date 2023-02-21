package com.jubotech.framework.netty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jubotech.framework.netty.processor.SocketMessageProcessor;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Service
@Sharable
@Slf4j
public class SocketProtoBufHandler extends ChannelInboundHandlerAdapter {
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	@Autowired
	private SocketMessageProcessor messageProcessor;
	 
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("socket login");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("socket normal exit");
		//NettyConnectionUtil.exit(ctx);
		nettyConnectionUtil.exit(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("socket读取并处理消息");
		//需要放到单独的分发器中处理
		messageProcessor.handler(ctx, msg);
		ctx.flush();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("socket  exception exit{}", cause.toString());
		//NettyConnectionUtil.exit(ctx);
		nettyConnectionUtil.exit(ctx);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		//NettyConnectionUtil.exit(ctx);
		nettyConnectionUtil.exit(ctx);
	}
 
}