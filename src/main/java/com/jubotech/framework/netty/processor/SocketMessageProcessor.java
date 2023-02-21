package com.jubotech.framework.netty.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.util.StringUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理分发器
 */

@Service
@Slf4j
public class SocketMessageProcessor {
	
	@Autowired
	private  Map<String,MessageHandler> handlers;
	 
	public  void  handler(ChannelHandlerContext ctx, Object msg) {
		TransportMessage transportMessage = (TransportMessage) msg;
		String type = StringUtil.toLowerCaseFirstOne(transportMessage.getMsgType().toString()+"Handler");
		log.debug("socket:"+type);
		if(handlers.containsKey(type)) {
			handlers.get(type).handleMsg(ctx, transportMessage);
		}else {
			log.error("not find msgtype"+type);
		}
		msg = null;
	}
	
	
}
