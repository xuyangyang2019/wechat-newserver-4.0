package com.jubotech.framework.netty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jubotech.framework.netty.processor.WebSocketMessageProcessor;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Service
@Sharable
@Slf4j
public class WebSocketJsonHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	@Autowired
	private WebSocketMessageProcessor messageProcessor;
	/**
	 * 覆盖了 channelRead0() 事件处理方法。 每当从服务端读到客户端写入信息时， 其中如果你使用的是 Netty 5.x 版本时， 需要把
	 * channelRead0() 重命名为messageReceived()
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		//System.out.println("收到消息：" + msg.text());
		//ctx.channel().writeAndFlush(new TextWebSocketFrame(msg.text()));
		messageProcessor.handler(ctx, msg);
		ctx.flush();
	}

	/**
	 * 每当从服务端收到新的客户端连接时
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// 打印出channel唯一值，asLongText方法是channel的id的全名
		log.info("handlerAdded：" + ctx.channel().id().asLongText());
	}

	/**
	 * 每当从服务端收到客户端断开时
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		log.info("handlerRemoved：" + ctx.channel().id().asLongText());
		//NettyConnectionUtil.exit(ctx);
		nettyConnectionUtil.exit(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("websocket exception exit{}",cause.toString());
		//NettyConnectionUtil.exit(ctx);
		nettyConnectionUtil.exit(ctx);
	}

}