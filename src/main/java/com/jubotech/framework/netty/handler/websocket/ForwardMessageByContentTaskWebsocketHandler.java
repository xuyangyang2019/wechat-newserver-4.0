package com.jubotech.framework.netty.handler.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.ForwardMessageByContentTask.ForwardMessageByContentTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ForwardMessageByContentTaskWebsocketHandler implements JsonMessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService;
	/**
	 * 转发微信消息内容
	 * @author wechatno:tangjinjinwx
	 * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo, String contentJsonStr) {
        try {
        	log.debug(contentJsonStr);
        	ForwardMessageByContentTaskMessage.Builder bd = ForwardMessageByContentTaskMessage.newBuilder();
        	JsonFormat.parser().merge(contentJsonStr, bd);
        	ForwardMessageByContentTaskMessage req = bd.build();
        	//将消息转发送给手机客户端
        	asyncTaskService.msgSend2Phone(ctx, req.getWeChatId(), EnumMsgType.ForwardMessageByContentTask, vo, req);
        	 
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
        }
    }
}