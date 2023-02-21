package com.jubotech.framework.netty.processor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.googlecode.protobuf.format.JsonFormat;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.util.StringUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理分发器
 */
@Service
@Slf4j
public class WebSocketMessageProcessor {
 
	@Autowired
	private Map<String, JsonMessageHandler> handlers;

	public void handler(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
		String text = msg.text();
		if (!StringUtils.isEmpty(text)) {
			try {
				JSONObject jsonObject = JSONObject.parseObject(text);
				Object objMsgType = jsonObject.get("MsgType");
				if (null == objMsgType) {
					log.error("MsgType消息类型传入错误！");
					MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, "MsgType消息类型传入错误");
					return;
				}
				 
				String contentJsonStr = jsonObject.getString("Content");
				if (org.apache.commons.lang3.StringUtils.isEmpty(contentJsonStr)) {
					log.error("Content消息类型传入错误！");
					MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, "Content消息类型传入错误");
					return;
				}
				
				TransportMessage.Builder builder = TransportMessage.newBuilder();
				String jsonFormat = jsonObject.toJSONString();
				log.error("请求json:"+jsonFormat);
				JsonFormat.merge(jsonFormat, builder);
				TransportMessage vo = builder.build();
				
				String msgtype = StringUtil.toLowerCaseFirstOne(objMsgType.toString() + "WebsocketHandler");
				log.debug("websocket:" + msgtype);
				if (handlers.containsKey(msgtype)) {
					handlers.get(msgtype).handleMsg(ctx, vo, contentJsonStr);
				}
				 
			} catch (Exception e) {
				e.printStackTrace();
				log.error("参数传入错误！"+e.getMessage());
				MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, "参数传入错误");
			}
		}
		text = null;// 清空一下
	}
	 
}
