package com.jubotech.framework.netty.handler.socket;

import org.springframework.stereotype.Service;

import com.jubotech.framework.netty.handler.MessageHandler;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.ErrorMessageOuterClass.ErrorMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class ErrorHandler implements MessageHandler{
	  
    public  void handleMsg(ChannelHandlerContext ctx, TransportMessage vo) {
        try {
        	ErrorMessage req = vo.getContent().unpack(ErrorMessage.class);
        	log.debug("错误信息:"+req.getErrorMsg());
        	//2、告诉客户端消息已收到
    		MessageUtil.sendMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}