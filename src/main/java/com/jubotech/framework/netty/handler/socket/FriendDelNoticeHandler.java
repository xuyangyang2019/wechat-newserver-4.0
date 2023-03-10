package com.jubotech.framework.netty.handler.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.FriendDelNotice.FriendDelNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
 
@Service
public class FriendDelNoticeHandler implements MessageHandler{
	@Autowired
	private WxAccountService weChatAccountService;
	@Autowired
	private AsyncTaskService asyncTaskService;
	@Autowired
	private WxContactService  weChatContactService;
 
	/**
	 * 微信删除好友通知消息
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
	@Async
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	FriendDelNoticeMessage req = vo.getContent().unpack(FriendDelNoticeMessage.class);
        	 
        	//把消息转发给pc端
    		asyncTaskService.msgSend2pc(req.getWeChatId(), EnumMsgType.FriendDelNotice, req);
    		
    		//好友删除通知
    		asyncTaskService.friendDeleteChangeService(req);
    		
    		//告诉客户端消息已收到
    		MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
    		if(null != account ){
    			WxContactInfo   contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),req.getWeChatId(),req.getFriendId());
        		if(null != contactinfo){
        			weChatContactService.delete(contactinfo.getId());
        		}
    		}
    		
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendErrMsg(ctx, EnumErrorCode.InvalidParam,vo.getId(), Constant.ERROR_MSG_DECODFAIL);
        }
    }
}