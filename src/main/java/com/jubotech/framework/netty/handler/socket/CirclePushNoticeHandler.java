package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.service.CircleService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.CirclePushNotice.CirclePushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CirclePushNoticeHandler implements MessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService; 
	
	@Autowired
	private CircleService circleService;
	
	@Autowired
	private WxContactService weChatContactService;
	
	/**
	 * 回传手机微信朋友圈数据
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	CirclePushNoticeMessage req = vo.getContent().unpack(CirclePushNoticeMessage.class);
        	log.debug(JsonFormat.printer().print(req));
        	//把消息转发给pc端
    		asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.CirclePushNotice, req);
    		
    		//保存朋友圈信息
    		asyncTaskService.asyncSaveCircleMsg(req, circleService, weChatContactService);
    		
        	//告诉客户端消息已收到
    		MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        }
    }
     
}