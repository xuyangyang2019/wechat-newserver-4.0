package com.jubotech.framework.netty.handler.websocket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.domain.SensitiveWords;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.service.SensitiveWordsService;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.framework.netty.async.AsyncTaskService;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.handler.JsonMessageHandler;
import com.jubotech.framework.netty.utils.ByteStringToString;
import com.jubotech.framework.netty.utils.MessageUtil;

import Jubo.JuLiao.IM.Wx.Proto.TalkToFriendTask.TalkToFriendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumContentType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumErrorCode;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TalkToFriendTaskWebsocketHandler  implements JsonMessageHandler{
	@Autowired
	private AsyncTaskService asyncTaskService;
	
	@Autowired
	private WxAccountService weChatAccountService;
	
	@Autowired
	private SensitiveWordsService sensitiveWordsService;
 
	/**
	 * 给微信好友发消息
	 * @author wechatno:tangjinjinwx
	 * @blog http://www.wlkankan.cn
	 */
	@Async
	public void handleMsg(ChannelHandlerContext ctx, TransportMessage vo, String contentJsonStr) {
		try {
			log.debug(contentJsonStr);
			TalkToFriendTaskMessage.Builder bd = TalkToFriendTaskMessage.newBuilder();
			JsonFormat.parser().merge(contentJsonStr, bd);
			TalkToFriendTaskMessage req = bd.build();
			
			WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
			if(null != account) {
				Integer cid = account.getCid();
				if(req.getContentType().equals(EnumContentType.Text)) {
					List<SensitiveWords> list = sensitiveWordsService.listByCid(cid);
					if(null != list && list.size()>0) {
						String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
						for(SensitiveWords word:list) {
							if(content.contains(word.getWords())) {
								MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, "消息含有敏感词，已被拦截");
								return;
							}
						}
					}
					
				}
				
				// 消息记录数据库
				asyncTaskService.savePcMessage(req,account);
				// 将消息转发送给手机客户端
				asyncTaskService.msgSend2Phone(ctx, req.getWeChatId(), EnumMsgType.TalkToFriendTask, vo, req);
			}
			 
		} catch (Exception e) {
			e.printStackTrace();
			MessageUtil.sendJsonErrMsg(ctx, EnumErrorCode.InvalidParam, Constant.ERROR_MSG_DECODFAIL);
		}
	}

}