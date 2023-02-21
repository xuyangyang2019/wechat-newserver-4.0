package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.FriendChangeNotice.FriendChangeNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FriendChangeNoticeHandler implements MessageHandler{
  
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	/**
	 * 微信好友信息变更通知
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn	
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	FriendChangeNoticeMessage req = vo.getContent().unpack(FriendChangeNoticeMessage.class);
        	log.debug(JsonFormat.printer().print(req));
        	
        	asyncTaskService.updateFriendContactinfo(req);
			 
			asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.FriendChangeNotice, req);
			 
			// 告诉客户端消息已收到
			MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        }
    }
}