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
import Jubo.JuLiao.IM.Wx.Proto.TriggerMessageReadTask.TriggerMessageReadTaskMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TriggerMessageReadTaskWebsocketHandler  implements JsonMessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService;
	/**
	 * 通知微信将某个聊天窗口置为已读
	 * @author wechatno:tangjinjinwx
	 * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx,TransportMessage vo, String contentJsonStr) {
        try {
        	log.debug(contentJsonStr);
        	TriggerMessageReadTaskMessage.Builder bd = TriggerMessageReadTaskMessage.newBuilder();
        	JsonFormat.parser().merge(contentJsonStr, bd);
        	TriggerMessageReadTaskMessage req = bd.build();
        	//TriggerMessageReadTaskMessage req = vo.getContent().unpack(TriggerMessageReadTaskMessage.class);
        	//将消息转发送给手机客户端
        	asyncTaskService.msgSend2Phone(ctx, req.getWeChatId(), EnumMsgType.TriggerMessageReadTask, vo, req);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
        }
    }
}