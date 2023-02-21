package com.jubotech.framework.netty.handler.socket;
 
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.FriendTalkNotice.FriendTalkNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FriendTalkNoticeHandler  implements MessageHandler{
 
	@Autowired
	private AsyncTaskService asyncTaskService; 
	/**
	 * 微信好友发来聊天消息通知
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
	@Async
	public void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
		try {
			FriendTalkNoticeMessage req = vo.getContent().unpack(FriendTalkNoticeMessage.class);
			log.debug(JsonFormat.printer().print(req));
			
			log.debug(LocalDateTime.now()+" 微信好友发来聊天消息  对应的线程名: "+Thread.currentThread().getName());
			  
            //拦截消息
			asyncTaskService.msgAopTask(ctx,req,vo.getAccessToken(), vo.getId());
			//消息转发到pc端
			asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.FriendTalkNotice, req);
			 
        	// 告诉客户端消息已收到
        	MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
        	 
			//消息记录数据库
			asyncTaskService.saveMessage(req);
		    
			 
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), e.getMessage());
		}
	}
	

	 
}