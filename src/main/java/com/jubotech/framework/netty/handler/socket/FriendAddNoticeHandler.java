package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.FriendAddNotice.FriendAddNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;

 
@Service
public class FriendAddNoticeHandler implements MessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService;
 
	/**
	 * 微信新增好友通知
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	FriendAddNoticeMessage req = vo.getContent().unpack(FriendAddNoticeMessage.class);
        	 
    		//把消息转发给pc端
    		asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.FriendAddNotice, req);
    		
    		//保存新增好友
    		asyncTaskService.saveFriendAddContactinfo(req);
    		//好友增加日志
    		asyncTaskService.friendAddChangeService(req);
    		
    		//告诉客户端消息已收到
    		MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
    		  
			 
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        }
    }
     
}