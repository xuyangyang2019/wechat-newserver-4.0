package com.jubotech.framework.netty.handler.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import Jubo.JuLiao.IM.Wx.Proto.WeChatGroupSendTask.WeChatGroupSendTaskMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeChatGroupSendTaskWesocketHandler  implements JsonMessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService;

	/**
	 * 微信群发消息
	 * @author wechatno:tangjinjinwx
	 * @blog http://www.wlkankan.cn
	 */
	@Async
	public void handleMsg(ChannelHandlerContext ctx, TransportMessage vo, String contentJsonStr) {
		try {
			log.debug(contentJsonStr);
			WeChatGroupSendTaskMessage.Builder bd = WeChatGroupSendTaskMessage.newBuilder();
			JsonFormat.parser().merge(contentJsonStr, bd);
			WeChatGroupSendTaskMessage req = bd.build();

			asyncTaskService.msgSend2Phone(ctx, req.getWeChatId(), EnumMsgType.WeChatGroupSendTask, vo, req);
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
		}
	}
}