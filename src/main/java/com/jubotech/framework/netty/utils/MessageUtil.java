package com.jubotech.framework.netty.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import com.jubotech.framework.proxy.ProxyUtil;

import Jubo.JuLiao.IM.Wx.Proto.ErrorMessageOuterClass.ErrorMessage;
import Jubo.JuLiao.IM.Wx.Proto.TalkToFriendTask.TalkToFriendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumContentType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageUtil {
	static NettyConnectionUtil nettyConnectionUtil = ProxyUtil.getBean(NettyConnectionUtil.class);
	
	/**
	 * 发送消息
	 * 
	 * @param ctx
	 * @param msgType
	 * @param accessToken
	 * @param refMsgId
	 * @param resp
	 */
	public static void sendMsg(ChannelHandlerContext ctx, EnumMsgType msgType, String accessToken, Long refMsgId,
			Message resp) {
		Message msg = MessageUtil.getMessage(msgType, accessToken, refMsgId, resp);
		ctx.channel().writeAndFlush(msg);
	}

	/**
	 * 发送json字符串消息
	 * 
	 * @param ctx
	 * @param msgType
	 * @param accessToken
	 * @param refMsgId
	 * @param resp
	 */
	public static void sendJsonMsg(ChannelHandlerContext ctx, EnumMsgType msgType, String accessToken, Long refMsgId,
			Message resp) {
		// Message msg = MessageUtil.getMessage(msgType,accessToken,refMsgId,
		// resp);
		// String json = JsonFormat.printToString(msg);
		// ctx.channel().writeAndFlush(json);
		String json = null;
		if (null != resp) {
			try {
				json = JsonFormat.printer().print(resp);
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		}
		JSONObject obj = new JSONObject();
		obj.put("msgType", msgType.name());
		obj.put("accessToken", accessToken);
		obj.put("refMsgId", refMsgId);
		obj.put("message", json);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(obj.toJSONString()));
	}

	/**
	 * 发送错误消息
	 * 
	 * @param ctx
	 * @param errorCode
	 * @param errMsg
	 */
	public static void sendErrMsg(ChannelHandlerContext ctx, EnumErrorCode errorCode,Long refMsgId, String errMsg) {
		ErrorMessage resp = MessageUtil.getErrMessage(errorCode, errMsg);
		sendMsg(ctx, EnumMsgType.Error, null, refMsgId, resp);
	}

	/**
	 * 发送json错误消息
	 * 
	 * @param ctx
	 * @param errorCode
	 * @param errMsg
	 */
	public static void sendJsonErrMsg(ChannelHandlerContext ctx, EnumErrorCode errorCode, String errMsg) {
		ErrorMessage resp = MessageUtil.getErrMessage(errorCode, errMsg);
		sendJsonMsg(ctx, EnumMsgType.Error, null, null, resp);
	}

	/**
	 * 封装错误消息消息体
	 */
	private static ErrorMessage getErrMessage(EnumErrorCode errorCode, String errorMsg) {
		return ErrorMessage.newBuilder().setErrorCode(errorCode).setErrorMsg(errorMsg).build();
	}

	/**
	 * 封装base消息体
	 * 
	 * @param type
	 * @param accessToken
	 * @param refMsgId
	 * @param resp
	 * @return
	 */
	private static Message getMessage(EnumMsgType type, String accessToken, Long refMsgId, Message resp) {
		TransportMessage.Builder builder = TransportMessage.newBuilder();

		builder.setId(MsgIdBuilder.getId());
		
		if (null != accessToken) {
			builder.setAccessToken(accessToken);
		}
		if (null != type) {
			builder.setMsgType(type);
		}
		if (null != refMsgId) {
			builder.setRefMessageId(refMsgId);
		}
		if (null != resp) {
			builder.setContent(Any.pack(resp));
		}
		return builder.build();
	}

	public static void sendCustomJsonMsg(ChannelHandlerContext ctx, String msgType, String json) {
		JSONObject obj = new JSONObject();
		obj.put("msgType", msgType);
		obj.put("message", json);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(obj.toJSONString()));
	}
	
	 /**
     * 给指定用户发文字消息
     * @param weChatId
     * @param friendWeChatId
     * @param content
     */
    public static void sendTextMsg(String weChatId,String friendWeChatId,String content) {
        log.info("发送消息......{}",content);
        ByteString byteString = ByteString.copyFromUtf8(content);
        //当前发消息的微信的通道
        ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(weChatId);
        if (chx == null) {
            log.error("微信通道未找到{}",weChatId);
            return;
        }
        sendMessage(chx, friendWeChatId, byteString, EnumContentType.Text);
    }
    
    
    /**
     * 给指定用户发指定类型消息
     * @param weChatId
     * @param friendWeChatId
     * @param content
     */
    public static void sendMsgByType(String weChatId,String friendWeChatId,String content,int type) {
        log.info("发送消息......{}",content);
        ByteString byteString = ByteString.copyFromUtf8(content);
        //当前发消息的微信的通道
        ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(weChatId);
        if (chx == null) {
            log.error("微信通道未找到{}",weChatId);
            return;
        }
        EnumContentType contentType = EnumContentType.forNumber(type);
        sendMessage(chx, friendWeChatId, byteString, contentType);
    }
    
    
	private static void sendMessage(ChannelHandlerContext ctx, String friendWeChatId, ByteString byteString, EnumContentType value) {
		//转发给手机端
        TalkToFriendTaskMessage.Builder bd = TalkToFriendTaskMessage.newBuilder();
        bd.setContent(byteString);
        bd.setContentType(value);
        //发给谁
        bd.setFriendId(friendWeChatId);
        bd.setMsgId(MsgIdBuilder.getId());
        TalkToFriendTaskMessage req2 = bd.build();
        MessageUtil.sendMsg(ctx, EnumMsgType.TalkToFriendTask, null, null, req2);
	}

}
