package com.jubotech.framework.netty.handler.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountLogoutNoticeWebsocketHandler implements JsonMessageHandler{
	
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	@Autowired
	private WxAccountService weChatAccountService;
	 
	/** 
	 * pc客户端退出通知
	 * @author wechatno:tangjinjinwx
     * @blog http://www.wlkankan.cn
	 */
    public  void handleMsg(ChannelHandlerContext ctx ,TransportMessage vo, String contentJsonStr) {
        try {
        	log.debug(contentJsonStr);
//        	AccountLogoutNoticeMessage.Builder bd = AccountLogoutNoticeMessage.newBuilder();
//        	JsonFormat.parser().merge(contentJsonStr, bd);
//    		AccountLogoutNoticeMessage req = bd.build();
        	  
        	//2、刷新缓存
			weChatAccountService.refreshCache();
        	
        	//3、告诉客户端消息已收到
    		MessageUtil.sendJsonMsg(ctx, EnumMsgType.DeviceExitNotice, vo.getAccessToken(), vo.getId(), null);
            //关闭连接
    		nettyConnectionUtil.exit(ctx);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
        }
    }
}