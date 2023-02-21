package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import Jubo.JuLiao.IM.Wx.Proto.WeChatTalkToFriendNotice.WeChatTalkToFriendNoticeMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class WeChatTalkToFriendNoticeHandler implements MessageHandler{
	 
	@Autowired
	private AsyncTaskService asyncTaskService;
	 
	/**
	 * 手机上回复好友的聊天消息 
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	WeChatTalkToFriendNoticeMessage req = vo.getContent().unpack(WeChatTalkToFriendNoticeMessage.class);
        	log.debug(JsonFormat.printer().print(req));
        	
			// 把消息转发给pc端
			asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.WeChatTalkToFriendNotice, req);
			
			//消息记录数据库
			asyncTaskService.savePhoneSendMessage(req);
			
        	// 告诉客户端消息已收到
			MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解码失败====WeChatTalkToFriendNotice=====", e);
			MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), e.getMessage());
        }
    }
     
}