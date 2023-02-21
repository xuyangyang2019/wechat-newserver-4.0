package com.jubotech.framework.netty.async;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.jubotech.business.web.dao.FriendAddTaskDao;
import com.jubotech.business.web.dao.FriendAddTaskDetailsDao;
import com.jubotech.business.web.dao.PhoneNumberDao;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.CallMessage;
import com.jubotech.business.web.domain.ChatGPTConversation;
import com.jubotech.business.web.domain.CircleComment;
import com.jubotech.business.web.domain.CircleLike;
import com.jubotech.business.web.domain.CircleTask;
import com.jubotech.business.web.domain.CircleTaskDetails;
import com.jubotech.business.web.domain.ConvDelLog;
import com.jubotech.business.web.domain.FriendAddTask;
import com.jubotech.business.web.domain.FriendAddTaskDetails;
import com.jubotech.business.web.domain.FriendChange;
import com.jubotech.business.web.domain.KeyWords;
import com.jubotech.business.web.domain.MsgDelLog;
import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.domain.Resources;
import com.jubotech.business.web.domain.SMSMessage;
import com.jubotech.business.web.domain.SysAutoSetting;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.domain.WxLuckymoney;
import com.jubotech.business.web.domain.WxMessageInfo;
import com.jubotech.business.web.service.AccountService;
import com.jubotech.business.web.service.CallMessageService;
import com.jubotech.business.web.service.ChatGPTConversationService;
import com.jubotech.business.web.service.CircleCommentService;
import com.jubotech.business.web.service.CircleLikeService;
import com.jubotech.business.web.service.CircleService;
import com.jubotech.business.web.service.CircleTaskService;
import com.jubotech.business.web.service.ConvDelLogService;
import com.jubotech.business.web.service.FriendChangeService;
import com.jubotech.business.web.service.KeyWordsService;
import com.jubotech.business.web.service.MsgDelLogService;
import com.jubotech.business.web.service.ResourcesService;
import com.jubotech.business.web.service.SMSMessageService;
import com.jubotech.business.web.service.SysAutoSettingService;
import com.jubotech.business.web.service.TimeTaskDetailsService;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.business.web.service.WxLuckymoneyService;
import com.jubotech.business.web.service.WxMessageService;
import com.jubotech.framework.common.AutoType;
import com.jubotech.framework.common.Constants;
import com.jubotech.framework.netty.common.Constant;
import com.jubotech.framework.netty.utils.ByteStringToString;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;
import com.jubotech.framework.util.ChatGPTUtils;
import com.jubotech.framework.util.DateUtil;
import com.jubotech.framework.util.EmojiFilter;
import com.jubotech.framework.util.JsonUtils;

import Jubo.JuLiao.IM.Wx.Proto.AcceptFriendAddRequestTask.AcceptFriendAddRequestTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.AgreeJoinChatRoomTask.AgreeJoinChatRoomTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.CallLogPushNotice.CallLogPushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.ChatRoomMembersNotice.ChatRoomMembersNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.ChatRoomMembersNotice.StrangerMessage;
import Jubo.JuLiao.IM.Wx.Proto.ChatRoomPushNotice.ChatRoomMessage;
import Jubo.JuLiao.IM.Wx.Proto.ChatRoomPushNotice.ChatRoomPushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleCommentNotice.CircleCommentNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleDelNotice.CircleDelNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleDetailNotice.CircleDetailNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleLikeNotice.CircleLikeNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.CirclePushNotice.CirclePushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.ConvDelNotice.ConvDelNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendAddNotice.FriendAddNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendAddNotice.FriendMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendAddReqListNotice.FriendAddReqListNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendAddReqListNotice.FriendReqMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendChangeNotice.FriendChangeNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendDelNotice.FriendDelNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendPushNotice.FriendPushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.FriendTalkNotice.FriendTalkNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.MsgDelNotice.MsgDelNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTaskResultNotice.PostSNSNewsTaskResultNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTaskResultNotice.PostSNSNewsTaskResultNoticeMessage.ExtraProperties;
import Jubo.JuLiao.IM.Wx.Proto.ScreenShotTaskResultNotice.ScreenShotTaskResultNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.SendLuckyMoneyTask.SendLuckyMoneyTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.SmsPushNotice.SmsPushNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TakeLuckyMoneyTask.TakeLuckyMoneyTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TakeMoneyTaskResultNotice.TakeMoneyTaskResultNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TalkToFriendTask.TalkToFriendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TaskResultNotice.TaskResultNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumContentType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.TransportMessage;
import Jubo.JuLiao.IM.Wx.Proto.WeChatOfflineNotice.WeChatOfflineNoticeMessage;
import Jubo.JuLiao.IM.Wx.Proto.WeChatTalkToFriendNotice.WeChatTalkToFriendNoticeMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableAsync
@Slf4j
public class AsyncTaskService {
	@Autowired
	private AccountService accountService;

	@Autowired
	private WxMessageService weChatMessageService;

	@Autowired
	private WxContactService weChatContactService;

	@Autowired
	private WxAccountService weChatAccountService;
	
	@Autowired
	private KeyWordsService keyWordsService;

	@Autowired
	private SysAutoSettingService sysAutoSettingService;

	@Autowired
	private  FriendAddTaskDetailsDao friendAddTaskDetailsDao;
	
	@Autowired
	private  PhoneNumberDao phoneNumberDao;
	
	@Autowired
	private  FriendAddTaskDao  friendAddTaskDao;
	 
	@Autowired
	private  CallMessageService callMessageService;
	
	@Autowired
	private  SMSMessageService smsMessageService;
	
	@Autowired
	private  TimeTaskDetailsService  timeTaskDetailsService;
	
	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	
	@Autowired
	private CircleLikeService circleLikeService;

	@Autowired
	private CircleCommentService circleCommentService;
	
	@Autowired
	private CircleTaskService circleTaskService;
	 
	@Autowired
	private WxLuckymoneyService wxLuckymoneyService;
	
	@Autowired
	private FriendChangeService friendChangeService;
	
	@Autowired
	private MsgDelLogService msgDelLogService;
	
	@Autowired
	private ConvDelLogService convDelLogService;
	
	@Autowired
	private ResourcesService resourcesService;
	
	@Autowired
	private ChatGPTConversationService chatGPTConversationService;
	
	
	/**
	 * 自动抢红包、自动收账
	 * @param ctx
	 * @param type
	 * @param req
	 * @param accessToken
	 * @param msgId
	 */
	@Async
	private  void takeLuckyMoney(ChannelHandlerContext ctx,Integer type,FriendTalkNoticeMessage req,String accessToken,long msgId,String remarks){
		String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
		if(type == AutoType.AUTOTYPE_LUCKMONEY){//红包有个人红包和群红包
			if(!JsonUtils.isJson(content)){//群红包处理
				int index =  content.indexOf("{");
				if(index>0){
					content= content.substring(index,content.length());
				}
			}
		}
		JSONObject json = JSON.parseObject(content);
		String key = null;
		if (null != json) {
			key = json.get("Key").toString();
		}
		if (!StringUtils.isBlank(key)) {
			TakeLuckyMoneyTaskMessage.Builder bd = TakeLuckyMoneyTaskMessage.newBuilder();
			bd.setWeChatId(req.getWeChatId());
			bd.setFriendId(req.getFriendId());
			bd.setMsgSvrId(req.getMsgSvrId());
			bd.setMsgKey(key);
			bd.setTaskId(MsgIdBuilder.getId());
			TakeLuckyMoneyTaskMessage resp = bd.build();
			MessageUtil.sendMsg(ctx, EnumMsgType.TakeLuckyMoneyTask,accessToken,msgId,resp);
			
			
			if(!StringUtils.isEmpty(remarks)) {
				sayHello(req.getWeChatId(), req.getFriendId(),remarks);
			}
			
		}
		
	}
	
	/**
	 * 加入群聊
	 * @param ctx
	 * @param type
	 * @param req
	 * @param accessToken
	 * @param msgId
	 */
	@Async
	private void agreeJoinChatRoom(ChannelHandlerContext ctx,Integer type, FriendTalkNoticeMessage req, String accessToken,long msgId) {
		String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
		if(JsonUtils.isJson(content)){
			int index =  content.indexOf("{");
			if(index>0){
				content= content.substring(index,content.length());
			}
			JSONObject json = JSON.parseObject(content);
			Object obj = json.get("Title");
			if (null != obj) {
				String title = obj.toString();
				if (title.contains("邀请你加入群聊")) {
					SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_GROUPINVITATION);
					if (null != sys && sys.getState()==0) {
						String talker = req.getFriendId();
						AgreeJoinChatRoomTaskMessage.Builder bd = AgreeJoinChatRoomTaskMessage.newBuilder();
						bd.setWeChatId(req.getWeChatId());
						bd.setMsgSvrId(req.getMsgSvrId());
						bd.setTalker(talker);
						bd.setTaskId(MsgIdBuilder.getId());
						bd.setMsgContent(content);
						AgreeJoinChatRoomTaskMessage resp = bd.build();
						MessageUtil.sendMsg(ctx, EnumMsgType.AgreeJoinChatRoomTask,accessToken,msgId, resp);
					}
				}
			}
		}
	}
	
	/**
	 * 拦截手机端消息做一下自动功能
	 * 
	 * @param wechatId
	 * @param type
	 * @param req
	 */
	@Async
	public void msgAopTask(ChannelHandlerContext ctx, FriendTalkNoticeMessage req,String accessToken,long msgId) {

		try {
			log.info(LocalDateTime.now() + " msgAopTask 对应的线程名: " + Thread.currentThread().getName());
			if (null == req.getContent()) {
			     return;
			}
			
			if (req.getContentType().equals(EnumContentType.LuckyMoney)) {// 红包消息
				SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_LUCKMONEY);
				if (null != sys && sys.getState()==0) {
						takeLuckyMoney(ctx, AutoType.AUTOTYPE_LUCKMONEY, req, accessToken, msgId,sys.getRemarks());
				}
			}
			
			if (req.getContentType().equals(EnumContentType.MoneyTrans)) {// 转账消息
				SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_MONEYTRANS);
				if (null != sys && sys.getState()==0) {
						takeLuckyMoney(ctx, AutoType.AUTOTYPE_MONEYTRANS, req, accessToken, msgId,sys.getRemarks());
				}
			}
			 
			if (req.getContentType().equals(EnumContentType.Link)) {// 链接消息，判断是否是群邀请链接
			    agreeJoinChatRoom(ctx,AutoType.AUTOTYPE_GROUPINVITATION, req, accessToken, msgId);
			}
			
			

			if(req.getFriendId().startsWith("gh_")) {
				log.info("公众号消息丢弃【{}】", req.getContent().toStringUtf8());
				return;
			}

			if (req.getContentTypeValue() != 1) {//除了文本消息，其他全部丢弃
				log.info("【{}】消息丢弃【{}】", req.getContentTypeValue(), req.getContent().toStringUtf8());
				return;
			}
			
			//关键词
			if(!req.getFriendId().contains("chatroom")) {
				SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_KEYWORD_O2O);
				if (null != sys && sys.getState()==0) {
					//匹配关键词
					KeyWords kws = checkKeyWord(req);
					if(null != kws){
						MessageUtil.sendMsgByType(req.getWeChatId(),req.getFriendId(),kws.getReturnString(),kws.getResourceType());
						return;
					}
				}
				
				//检查是否开启chatGPT
				SysAutoSetting sys1 = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_CHATGPT);
				if (null != sys1 && sys1.getState()==0) {
					
					if(!StringUtils.isEmpty(sys1.getRemarks())) {
						MessageUtil.sendMsgByType(req.getWeChatId(),req.getFriendId(),sys1.getRemarks(),EnumContentType.Text_VALUE);
					}
					
					
					String message = req.getContent().toStringUtf8();
					ChatGPTConversation conversation = chatGPTConversationService.byWechatidFriendid(req.getWeChatId(), req.getFriendId());
					String conversationId=null;
					if(null != conversation) {
						conversationId =  conversation.getConversation();
					}
					Map<String, String> map = ChatGPTUtils.askOpenAi(message, conversationId);
					if(null != map && map.size()>0) {
						conversationId =  map.get("conversationId");
						if(!StringUtils.isEmpty(conversationId)) {
							if(null != conversation) {
								conversation.setConversation(conversationId);
								chatGPTConversationService.update(conversation);
							}else {
								conversation = new ChatGPTConversation();
								conversation.setWechatid(req.getWeChatId());
								conversation.setFriendid(req.getFriendId());
								conversation.setConversation(conversationId);
								chatGPTConversationService.insert(conversation);
							}
						}
						
						if(!StringUtils.isEmpty(map.get("message"))) {
							MessageUtil.sendMsgByType(req.getWeChatId(),req.getFriendId(),map.get("message"),EnumContentType.Text_VALUE);
						}
						
						return;
					}
					 
				}
				
			}else {
				SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_KEYWORD_O2M);
				if (null != sys && sys.getState()==0) {
					//匹配关键词
					KeyWords kws = checkKeyWord(req);
					if(null != kws){
						MessageUtil.sendMsgByType(req.getWeChatId(),req.getFriendId(),kws.getReturnString(),kws.getResourceType());
						return;
					}
				}
				 
			}
			
			//接个查券小插件（测试玩玩）
//			String msg = CouponUtil.request(req.getContent().toStringUtf8());
//			if(!StringUtils.isEmpty(msg)) {
//				MessageUtil.sendTextMsg(req.getWeChatId(),req.getFriendId(), msg);
//			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 关键词匹配
	 * @param req
	 * @return
	 */
	 private  KeyWords checkKeyWord(FriendTalkNoticeMessage req) {
	         try {
				String message = req.getContent().toStringUtf8();
				if(!StringUtils.isEmpty(message)){
					String [] msg = message.split(":");
					if(null != msg && msg.length>1) {
						message = msg[1].trim();
					}
					
					 //完全匹配
					 List<KeyWords> keyWords = keyWordsService.listByAccountIdKeyType(req.getWeChatId(),0);
					 if (!CollectionUtils.isEmpty(keyWords)) {
						 for(KeyWords kw:keyWords){
							 if(!StringUtils.isEmpty(kw.getKeyWord())) {
								 if(message.equals(kw.getKeyWord())){
									 return kw;
								 }
							 } 
						 }
						  
					 }
					 //模糊匹配
					 List<KeyWords> mkeyWords = keyWordsService.listByAccountIdKeyType(req.getWeChatId(),1);
					 if (!CollectionUtils.isEmpty(mkeyWords)) {
					     for(KeyWords kw:mkeyWords){
					    	 if(!StringUtils.isEmpty(kw.getKeyWord())){
								 if(message.contains(kw.getKeyWord())){
									 return kw;
								 }
					    	 }
						 }
					 }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		     
	        return null;
	 }
	
	 @Async
	 public void friendAddAutoPass(ChannelHandlerContext ctx, FriendAddReqListNoticeMessage req,String accessToken,long msgId) {
		 if(null != req) {
			List<FriendReqMessage> list =  req.getRequestsList();
			if(null != list && list.size()>0){
				for(FriendReqMessage friend:list) {
					if(friend.getState()==0 && friend.getFirstReq()>(System.currentTimeMillis() - (24 * 60 * 60 * 1000))  ) {
						msgAopTaskFriendAdd(ctx, req.getWeChatId(), friend.getFriendId(), friend.getFriendNick(), accessToken, msgId);
					}
				}
			}
		 }
	 }
	
	 
	
	/**
	 * 拦截手机端好友请求消息
	 * 
	 * @param wechatId
	 * @param type
	 * @param req
	 */
	@Async
	public void msgAopTaskFriendAdd(ChannelHandlerContext ctx,String wechatid,String friendid,String friendNick,String accessToken,long msgId) {
		try {
			log.info(LocalDateTime.now() + " msgAopTask 对应的线程名: " + Thread.currentThread().getName());
			 
			SysAutoSetting sys1 = sysAutoSettingService.findSettingByWcIdAutoType(wechatid,AutoType.AUTOTYPE_FRIENDREQEST_WW);
			if (null != sys1 && sys1.getState()==0) {
			 
				passFriendAdd(ctx, wechatid, friendid, friendNick, accessToken, msgId, true);
				return;
			}
			
			SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(wechatid,AutoType.AUTOTYPE_FRIENDREQEST);
			if (null != sys && sys.getState()==0){
				
				passFriendAdd(ctx, wechatid, friendid, friendNick, accessToken, msgId, false);
				return;
			}
			
			
			  
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
	private static void passFriendAdd(ChannelHandlerContext ctx,String wechatid,String friendid,String friendNick,String accessToken,long msgId,boolean withww){
		AcceptFriendAddRequestTaskMessage.Builder  bd = AcceptFriendAddRequestTaskMessage.newBuilder();
		bd.setWeChatId(wechatid);
		bd.setFriendId(friendid);
		bd.setFriendNick(friendNick);
		if(withww) {
			bd.setAddWithWW(true);
		}
		bd.setOperationValue(1);//默认接受请求
		AcceptFriendAddRequestTaskMessage resp = bd.build();
		MessageUtil.sendMsg(ctx, EnumMsgType.AcceptFriendAddRequestTask, accessToken, msgId, resp);
	}

	
	public  void sayHello(String wechatid,String friendid,String msg) {
		ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(wechatid);
		if (null != chx) {
			ByteString byteString = ByteString.copyFromUtf8(msg);
			TalkToFriendTaskMessage.Builder bd = TalkToFriendTaskMessage.newBuilder();
			bd.setContent(byteString);
	        bd.setContentType(EnumContentType.Text);
	        //发给谁
	        bd.setFriendId(friendid);
	        bd.setMsgId(MsgIdBuilder.getId());
			TalkToFriendTaskMessage req = bd.build();
		   MessageUtil.sendMsg(chx,  EnumMsgType.TalkToFriendTask, null, null, req);
		};
	}
	
	

	/**
	 * 转发消息给pc客户端
	 * 
	 * @param wechatId
	 * @param type
	 * @param req
	 */
	@Async
	public void msgSend2pc(String wechatId, EnumMsgType type, Message req) {

		try {
			
			if(StringUtils.isEmpty(wechatId)){
				return;
			}
			
			log.info(LocalDateTime.now() + " msgSend2pc 对应的线程名: " + Thread.currentThread().getName());
			
			 
			// 先从缓存取，如果取不到再用sql取
			List<WxAccountInfo> accountInfoList = Constant.accountInfoList;
			if (null == accountInfoList || accountInfoList.size() == 0) {
				// 刷新缓存
				accountInfoList = weChatAccountService.refreshCache();
			}

			WxAccountInfo account = null;
			if (null != accountInfoList && accountInfoList.size() > 0) {
				for (int i = 0; i < accountInfoList.size(); i++) {
					WxAccountInfo ainfo = accountInfoList.get(i);
					if (null != ainfo && null !=ainfo.getWechatid() && ainfo.getWechatid().equals(wechatId)) {
						account = ainfo;
						break;
					}
				}
			}
			
			if(null == account){
				WxAccountInfo	accountInfo = weChatAccountService.findWeChatAccountInfoByWeChatId(wechatId);
				if(null != accountInfo){
					account = accountInfo;
					weChatAccountService.refreshCache();
				}
			}  
			
			if (null != account && !StringUtils.isEmpty(account.getAccountid())) {
				List<AccountInfo> accInfo = accountService.queryByIds(account.getAccountid());
				if(null != accInfo){
					for(AccountInfo info:accInfo ){
						// 转发给pc端
						ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(info.getAccount());
						if (null != chx) {
							MessageUtil.sendJsonMsg(chx, type, nettyConnectionUtil.getNettyId(chx), null, req);
						}
					}
					
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	/**
	 * 转发消息给手机客户端
	 * 
	 * @param ctx
	 * @param wechatId
	 * @param type
	 * @param vo
	 * @param req
	 */
	@Async
	public void msgSend2Phone(ChannelHandlerContext ctx, String wechatId, EnumMsgType type, TransportMessage vo,Message req) {

		try {
			log.info(LocalDateTime.now() + " msgSend2Phone 对应的线程名: " + Thread.currentThread().getName());

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(wechatId);
			if (null != chx) {
				// 转发给手机端
				MessageUtil.sendMsg(chx, type, vo.getAccessToken(), null, req);
				// 告诉发送方 消息已经收到
				MessageUtil.sendJsonMsg(ctx, EnumMsgType.MsgReceivedAck, vo.getAccessToken(), vo.getId(), null);
			} else {
				// 做个保护，如果微信不在线，则通知pc端，下线其微信
				WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(wechatId);
				if(null != account){
					account.setIsonline(1);// 下线
					weChatAccountService.update(account);
				}
				
				// 通知pc端下线其微信
				WeChatOfflineNoticeMessage resp = WeChatOfflineNoticeMessage.newBuilder().setWeChatId(wechatId).build();
				MessageUtil.sendJsonMsg(ctx, EnumMsgType.WeChatOfflineNotice, nettyConnectionUtil.getNettyId(ctx),null, resp);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存通讯录录列表数据
	 * 
	 * @param req
	 * @param account
	 */
	@Async
	public void friendListSave(FriendPushNoticeMessage req) {
		log.info(LocalDateTime.now() + " friendListSave 对应的线程名: " + Thread.currentThread().getName());
		WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
		if(null == account){
			return;
		}
		List<FriendMessage> friendList = req.getFriendsList();
		if (null != friendList && friendList.size() > 0) {
			String wechatId = req.getWeChatId();
			for (int i = 0; i < friendList.size(); i++) {
				FriendMessage friend = friendList.get(i);
				if (null != friend) {
					try {
						WxContactInfo contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),
								wechatId, friend.getFriendId());
						if (null != contactinfo) {
							setContActinfo(contactinfo, friend);
							weChatContactService.update(contactinfo);
						} else {
							contactinfo = new WxContactInfo();
							setContActinfo(contactinfo, friend);
							contactinfo.setCid(account.getCid());
							contactinfo.setWechatid(req.getWeChatId());
							contactinfo.setFriendid(friend.getFriendId());
							weChatContactService.insert(contactinfo);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * 保存新增好友数据
	 * @param req
	 */
	@Async
	public  void saveFriendAddContactinfo(FriendAddNoticeMessage req){
    	try {
    		WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
    		if(null == account){
    			return;
    		}
    		
    		//自动打招呼
    		SysAutoSetting sys0 = sysAutoSettingService.findSettingByWcIdAutoType(req.getWeChatId(),AutoType.AUTOTYPE_SAYHELLO);
			if(null != sys0 && !StringUtils.isEmpty(sys0.getRemarks())) {
				sayHello(req.getWeChatId(), req.getFriend().getFriendId(), sys0.getRemarks());
			}
    		
    		 
			//新增好友
			FriendMessage  friend = req.getFriend();
			WxContactInfo   contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),req.getWeChatId(),friend.getFriendId());
			if(null != contactinfo){
				setContActinfo(contactinfo, friend);
				weChatContactService.update(contactinfo);
			}else{
				contactinfo = new WxContactInfo();
				setContActinfo(contactinfo, friend);
				contactinfo.setCid(account.getCid());
				contactinfo.setWechatid(req.getWeChatId());
				contactinfo.setFriendid(friend.getFriendId());
				weChatContactService.insert(contactinfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 更新好友数据
	 * @param req
	 */
	@Async
	public  void updateFriendContactinfo(FriendChangeNoticeMessage req){
    	try {
    		WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
    		if(null == account){
    			return;
    		}
			FriendMessage  friend = req.getFriend();
			WxContactInfo   contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),req.getWeChatId(),friend.getFriendId());
			if(null != contactinfo){
				setContActinfo(contactinfo, friend);
				weChatContactService.update(contactinfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	/**
	 * 更新陌生人信息
	 * @param req
	 */
	@Async
	public  void friendMembersNotice(ChatRoomMembersNoticeMessage req){
    	try {
    		 
    		List<StrangerMessage> friendList = req.getMembersList();
    		if (null != friendList && friendList.size() > 0) {
    			
    			String wechatId = req.getWeChatId();
    			if(StringUtils.isEmpty(wechatId)) {
    				return;
    			}
    			
    			WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(wechatId);
        		if(null == account){
        			return;
        		}
        		
    			for (int i = 0; i < friendList.size(); i++) {
    				StrangerMessage friend = friendList.get(i);
    				if (null != friend) {
    					try {
    						if(!StringUtils.isBlank(friend.getMemo())) {
    							WxContactInfo contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),wechatId, friend.getWxid());
        						if (null != contactinfo) {
        							contactinfo.setGender(friend.getGenderValue());
        							contactinfo.setAvatar(friend.getAvatar());
        							contactinfo.setCountry(friend.getCountry());
        							contactinfo.setProvince(friend.getProvince());
        							contactinfo.setCity(friend.getCity());
        							contactinfo.setMemo(friend.getMemo());
        							weChatContactService.update(contactinfo);
        						}
    						}
    					} catch (Throwable e) {
    						e.printStackTrace();
    					}
    				}
    			}
    		}
    		
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	
	private static void setContActinfo(WxContactInfo contactinfo, FriendMessage friend) {
		String nickName = EmojiFilter.filterEmoji(friend.getFriendNick());//过滤emoji
		contactinfo.setFriend_wechatno(friend.getFriendNo());
		contactinfo.setNickname(nickName);
		contactinfo.setGender(friend.getGenderValue());
		contactinfo.setAvatar(friend.getAvatar());
		contactinfo.setRemark(friend.getRemark());
		contactinfo.setCountry(friend.getCountry());
		contactinfo.setProvince(friend.getProvince());
		contactinfo.setCity(friend.getCity());
		contactinfo.setMemo(friend.getMemo());
		contactinfo.setType(0);
	}
	
	/**
	 * 保存群列表数据
	 * 
	 * @param req
	 * @param account
	 */
	@Async
	public void qunListSave(ChatRoomPushNoticeMessage req) {
		log.info(LocalDateTime.now() + " qunListSave 对应的线程名: " + Thread.currentThread().getName());
		WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
		if(null == account){
			return;
		}
		List<ChatRoomMessage> chatRoomList = req.getChatRoomsList();
		if (null != chatRoomList && chatRoomList.size() > 0) {
			String wechatId = req.getWeChatId();
			for (int i = 0; i < chatRoomList.size(); i++) {
				ChatRoomMessage friend = chatRoomList.get(i);
				if (null != friend) {
					try {
						WxContactInfo contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),wechatId, friend.getUserName());
						String nickName = EmojiFilter.filterEmoji(friend.getNickName());//过滤emoji
						if (null != contactinfo) {
							contactinfo.setNickname(nickName);
							contactinfo.setAvatar(friend.getAvatar());
							contactinfo.setRemark(friend.getRemark());
							
							weChatContactService.update(contactinfo);
						} else {
							contactinfo = new WxContactInfo();
							contactinfo.setNickname(nickName);
							contactinfo.setAvatar(friend.getAvatar());
							contactinfo.setRemark(friend.getRemark());
							contactinfo.setCid(account.getCid());
							contactinfo.setWechatid(req.getWeChatId());
							contactinfo.setFriendid(friend.getUserName());
							contactinfo.setType(1);
							weChatContactService.insert(contactinfo);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
 
	
	/**
	 * 修改任务结果
	 */
	@Async
	public void updateTaskResult(String msgId,String result) {
		try {
			log.info(LocalDateTime.now() + " updateTaskResult 对应的线程名: " + Thread.currentThread().getName());
			TimeTaskDetails details = timeTaskDetailsService.findTimeTaskDetailsByMsgId(msgId);
			if(null != details){
				details.setResults(result);
				timeTaskDetailsService.updateResults(details);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 修改发朋友圈任务结果
	 */
	@Async
	public void updateCircleTaskResult(PostSNSNewsTaskResultNoticeMessage req) {
		try {
			log.info(LocalDateTime.now() + " updateTaskResult 对应的线程名: " + Thread.currentThread().getName());
			updateTaskResult(String.valueOf(req.getTaskId()),String.valueOf(req.getSuccess()));
			ExtraProperties ext = req.getExtra();
			if(null != ext) {
				CircleTaskDetails details = circleTaskService.findByWechatIdMsgId(req.getWeChatId(), String.valueOf(req.getTaskId()));
				if(null != details){
					details.setMsgid(String.valueOf(ext.getCircleId()));
					details.setResults(String.valueOf(req.getSuccess()));
					circleTaskService.updateDetail(details);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 修改msgserverid
	 */
	@Async
	public void updateMsgServerId(String weChatId, String friendId, long msgId, long msgServerId) {
		try {
			if(!Constants.MSG_SAVE_FLAG) {
				return;
			}
			
			log.info(LocalDateTime.now() + " updateMsgServerId 对应的线程名: " + Thread.currentThread().getName());
			WxMessageInfo msgInfo = weChatMessageService.queryWeChatMessageInfoByMsgServerId(weChatId, friendId,String.valueOf(msgId));
			if (null != msgInfo) {
				msgInfo.setMsgsvrid(String.valueOf(msgServerId));
				weChatMessageService.update(msgInfo);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 修改图片，视频，文件地址
	 */
	@Async
	public void updateFileMsgContent(String weChatId,String content, long msgServerId) {
		try {
			
			if(!Constants.MSG_SAVE_FLAG) {
				return;
			}
			 
			log.info(LocalDateTime.now() + " updateFileMsgContent 对应的线程名: " + Thread.currentThread().getName());
			WxMessageInfo msgInfo = weChatMessageService.queryWeChatMessageInfoByMsgServerId(weChatId, null,String.valueOf(msgServerId));
			if (null != msgInfo) {
				msgInfo.setContent(content);
				weChatMessageService.updateContent(msgInfo);
			}
			 
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存消息
	 * 
	 * @param weChatMessageService
	 * @param account
	 * @param req
	 */
	@Async
	public void saveMessage(FriendTalkNoticeMessage req) {
		try {

			if(!Constants.MSG_SAVE_FLAG) {
				return;
			}
			 
			log.info(LocalDateTime.now() + " saveMessage 对应的线程名: " + Thread.currentThread().getName());
	        WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
			if (null != account) {
				WxContactInfo wxContact =	weChatContactService.findContactinfoByfriendid(account.getCid(), req.getWeChatId(), req.getFriendId());
    			if(null != wxContact) {
					WxMessageInfo info = new WxMessageInfo();
					String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
					info.setCid(account.getCid());
					info.setWechatid(req.getWeChatId());
					info.setWechatno(account.getWechatno());
					info.setWechatnick(account.getWechatnick());
					info.setFriendid(req.getFriendId());
					info.setFriendnick(wxContact.getNickname());
					info.setMsgsvrid(String.valueOf(req.getMsgSvrId()));
					info.setIssend("false");// 收到的消息
					info.setContenttype(req.getContentTypeValue());
					info.setContent(content);// base64了 需要解码存储
					info.setCreateTime(new Date(req.getCreateTime()));
					if(req.getFriendId().contains("chatroom")){
						info.setType(1);
						info.setFriendno(req.getFriendId());
					}else{
						info.setType(0);
					}
					weChatMessageService.insert(info);
    			}
			}	
		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存发送消息
	 * 
	 * @param req
	 */
	@Async
	public void savePcMessage(TalkToFriendTaskMessage req,WxAccountInfo account) {
		log.info(LocalDateTime.now() + " savePcMessage 对应的线程名: " + Thread.currentThread().getName());
		
		if(!Constants.MSG_SAVE_FLAG) {
			return;
		}
		 
		try {
			WxContactInfo wxContact =	weChatContactService.findContactinfoByfriendid(account.getCid(), req.getWeChatId(), req.getFriendId());
			if(null != wxContact) {
				WxMessageInfo info = new WxMessageInfo();
				String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
				info.setCid(account.getCid());
				info.setWechatid(req.getWeChatId());
				info.setWechatno(account.getWechatno());
				info.setWechatnick(account.getWechatnick());
				info.setFriendid(req.getFriendId());
				info.setFriendno(wxContact.getFriend_wechatno());
				info.setFriendnick(wxContact.getNickname());
				info.setMsgsvrid(String.valueOf(req.getMsgId()));
				info.setIssend("true");// 发送
				info.setContenttype(req.getContentTypeValue());
				info.setContent(content);// base64了 需要解码存储
				info.setCreateTime(new Date());
				if(req.getFriendId().contains("chatroom")){
					info.setType(1);
					info.setFriendno(req.getFriendId());
				}else{
					info.setType(0);
				}
				weChatMessageService.insert(info);
			}	
		} catch (Throwable e) {
			e.printStackTrace();
		}
		 
		 
	}

	/**
	 * 保存红包消息
	 * 
	 * @param req
	 * @param contentJsonStr
	 */
	@Async
	public void saveLuckyMoneyMessage(SendLuckyMoneyTaskMessage req, String contentJsonStr) {
		log.info(LocalDateTime.now() + " saveLuckyMoneyMessage 对应的线程名: " + Thread.currentThread().getName());
		
		if(!Constants.MSG_SAVE_FLAG) {
			return;
		}
		 
		WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
		if (null != account) {
			try {
				WxContactInfo wxContact =	weChatContactService.findContactinfoByfriendid(account.getCid(), req.getWeChatId(), req.getFriendId());
    			if(null != wxContact) {
					WxMessageInfo info = new WxMessageInfo();
					String content = contentJsonStr;
					info.setCid(account.getCid());
					info.setWechatid(req.getWeChatId());
					info.setWechatno(account.getWechatno());
					info.setWechatnick(account.getWechatnick());
					info.setFriendid(req.getFriendId());
					info.setFriendno(wxContact.getFriend_wechatno());
					info.setFriendnick(wxContact.getNickname());
					info.setMsgsvrid(String.valueOf(req.getTaskId()));
					info.setIssend("true");// 发送
					info.setContenttype(11);
					info.setContent(content);
					info.setCreateTime(new Date());
					if(req.getFriendId().contains("chatroom")){
						info.setType(1);
						info.setFriendno(req.getFriendId());
					}else{
						info.setType(0);
					}
					weChatMessageService.insert(info);
    			}	
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		 
	}
	
	/**
	 * 手机上回复了好友消息，消息记录上传
	 * @param req
	 */
	@Async
	public  void savePhoneSendMessage(WeChatTalkToFriendNoticeMessage req){

		if(!Constants.MSG_SAVE_FLAG) {
			return;
		}

	    	WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		try {
	    			WxContactInfo wxContact =	weChatContactService.findContactinfoByfriendid(account.getCid(), req.getWeChatId(), req.getFriendId());
	    			if(null != wxContact) {
	    				WxMessageInfo info = new WxMessageInfo();
						String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
						info.setCid(account.getCid());
						info.setWechatid(req.getWeChatId());
						info.setWechatno(account.getWechatno());
						info.setWechatnick(account.getWechatnick());
						info.setFriendid(req.getFriendId());
						info.setFriendno(wxContact.getFriend_wechatno());
						info.setFriendnick(wxContact.getNickname());
						info.setMsgsvrid(String.valueOf(req.getMsgSvrId()));
						info.setIssend("true");//发送
						info.setContenttype(req.getContentTypeValue());
						info.setContent(content);//base64了 需要解码存储
						info.setCreateTime(new Date(req.getCreateTime()));
						if(req.getFriendId().contains("chatroom")){
							info.setType(1);
							info.setFriendno(req.getFriendId());
						}else{
							info.setType(0);
						}
						weChatMessageService.insert(info);
	    			}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	    
	}
	
	
	/**
	 * 保存红包转账通知
	 * @param req
	 */
	@Async
	public void saveLuckyMoneyMessage(TakeMoneyTaskResultNoticeMessage req) {
		try {
 
			log.info(LocalDateTime.now() + " saveMessage 对应的线程名: " + Thread.currentThread().getName());
	        WxAccountInfo account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
			if (null != account) {
				String nickName = EmojiFilter.filterEmoji(req.getSenderName());//过滤emoji
				WxLuckymoney info = new WxLuckymoney();
				info.setCid(account.getCid());
				info.setWechatid(req.getWeChatId());
				info.setFriendid(req.getSender());
				info.setFriendname(nickName);
				info.setAmount(req.getAmount());
				info.setType(req.getType());
				info.setMsgid(req.getTaskId()+"");
				info.setContent(req.getMsgKey());
				info.setCreateTime(new Date());
				 
				wxLuckymoneyService.insert(info);
			}	
		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 修改加好友任务结果
	 * @param req
	 */
	@Async
	public  void updateFriendAddMessage(TaskResultNoticeMessage req){
	    try {
			if(null != req && req.getTaskType()==EnumMsgType.AddFriendsTask){
				Long  taskid = req.getTaskId();
				if(null != taskid){
					FriendAddTaskDetails taskDetails = friendAddTaskDetailsDao.findFriendAddTaskDetailsByid(taskid.intValue());
					if(null != taskDetails){
						if(taskDetails.getWechatid().equals(req.getWeChatId())){
							List<PhoneNumberInfo> phoneNumberInfo = phoneNumberDao.queryPhoneNumberInfoByWechatidPhoneNumber(taskDetails.getWechatid(), taskDetails.getPhonenumber());
							if(null != phoneNumberInfo){
								if(req.getSuccess()){
									for(PhoneNumberInfo info :phoneNumberInfo){
										info.setTask_result("success");
										phoneNumberDao.update(info);
									}
									
									FriendAddTask friendAddTask = friendAddTaskDao.findFriendAddTaskByid(taskDetails.getTid());
									if(null != friendAddTask){
										Integer newsussesize = friendAddTask.getSuccesssize()+1;
										friendAddTask.setSuccesssize(newsussesize);
										friendAddTaskDao.update(friendAddTask);
									}
									
								}else{
									for(PhoneNumberInfo info :phoneNumberInfo){
										info.setTask_result(req.getErrMsg());
										phoneNumberDao.update(info);
									}
								}
								
								 
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 保存通话记录
	 * @param req
	 */
	@Async
	public  void saveCallLogMessage(CallLogPushNoticeMessage req){
	    	WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		try {
	    			CallMessage call = new CallMessage();
	    			call.setCid(account.getCid());
	    			call.setNumber(req.getMessages().getNumber());
	    			call.setType(req.getMessages().getType());
	    			call.setDate(req.getMessages().getDate());
	    			call.setDuration(req.getMessages().getDuration());
	    			call.setRecord(req.getMessages().getRecord());
	    			call.setWechatid(req.getWeChatId());
	    			call.setImei(req.getIMEI());
	    			call.setBlocktype(req.getMessages().getBlockType());
	    			call.setCreateTime(new Date());
	    			callMessageService.insert(call);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	}
	 
	/**
	 * 保存手机短信记录
	 * @param req
	 */
	@Async
	public  void saveSmsPushNoticeMessage(SmsPushNoticeMessage req){
	    	WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		try {
	    			SMSMessage call = new SMSMessage();
	    			call.setWechatid(req.getWeChatId());
	    			call.setImei(req.getIMEI());
	    			call.setCid(account.getCid());
	    			call.setNumber(req.getMessages().getNumber());
	    			call.setType(req.getMessages().getType());
	    			call.setDate(req.getMessages().getDate());
	    			call.setContent(req.getMessages().getContent());
	    			call.setReadz(String.valueOf(req.getMessages().getRead()));
	    			call.setSimid(req.getMessages().getSimId());
	    			call.setBlocktype(req.getMessages().getBlockType());
	    			call.setCreateTime(new Date());
	    			smsMessageService.insert(call);
				} catch (Exception e) {
					e.printStackTrace();
				}
	    	}
	}
	
	
	/**
     * 倔牛 独有的业务
     * @param req
     */
    @Async
    public void asyncSaveCircleMsg(CirclePushNoticeMessage req,CircleService circleService,WxContactService weChatContactService){
    	try {
    		/*
			String  wechatid = req.getWeChatId();
			//爱咋咋地
			List<CircleInformationMessage> list = req.getCirclesList();
			if(null != list && list.size()>0){
				for(int i=0;i<list.size();i++){
					CircleInformationMessage circle =  list.get(i);
					CircleContentMessage content = circle.getContent();
					String text = content.getText();
					List<CircleNewsContentMessage> images =  content.getImagesList();//获取缩略图列表
					CircleNewsContentMessage  link = content.getLink();//获取链接
					CircleNewsContentMessage video = content.getVideo();//获取视频缩略图
					String thumbimages = "";
					if(null != images && images.size()>0){
						for(int j=0;j<images.size();j++){
							CircleNewsContentMessage imgs= 	images.get(j);
							if(null != imgs && StringUtils.isNoneEmpty(imgs.getThumbImg())){
								thumbimages = thumbimages + imgs.getThumbImg() + ",";
							}
						}
					}
					
					if(!StringUtils.isBlank(thumbimages) && thumbimages.endsWith(",")){
						thumbimages = thumbimages.substring(0, thumbimages.length()-1);
					}
					
					WxContactInfo  contcact =weChatContactService.findContactinfoByWechatidFriendid(wechatid, circle.getWeChatId());
					CircleInfo ccl = new CircleInfo();
					ccl.setSourcewechatid(wechatid);//抓取者
					ccl.setWechatid(circle.getWeChatId());//发布朋友圈的人
                    if(null != contcact){
                    	ccl.setWechatnickname(contcact.getNickname());
					}
					ccl.setCircleid(String.valueOf(circle.getCircleId()));
					ccl.setContent(text);
					ccl.setPublishtime(String.valueOf(circle.getPublishTime()));
					ccl.setThumbimages(thumbimages);
					if(null != link){
						ccl.setLink(link.getUrl());
					}
					if(null != video ){
						ccl.setVideothumbimg(video.getThumbImg());
						ccl.setVideodescription(video.getDescription());
						ccl.setVideomediaid(video.getMediaId());
					}
					
					circleService.insert(ccl);
					
				}
			}
			*/
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
	
    
    /**
     * 倔牛 独有的业务
     * @param req
     */
    @Async
    public  void asyncUpdateCircleMsg(CircleDetailNoticeMessage req,CircleService circleService){
    	try {
		 /*
			CircleInformationMessage  circle = req.getCircle();
			if(null != circle){
				CircleContentMessage  content = circle.getContent();
				List<CircleNewsContentMessage> newContent = content.getImagesList();
				String imgUrls = "";
				if(null != newContent && newContent.size()>0){
					for(int i=0;i<newContent.size();i++){
						CircleNewsContentMessage news  = newContent.get(i);
						if(null != news && !StringUtils.isBlank(news.getUrl())){
							imgUrls =  imgUrls + news.getUrl()+ ",";
						}
					}
				
					if(!StringUtils.isBlank(imgUrls) && imgUrls.endsWith(",")){
						imgUrls = imgUrls.substring(0, imgUrls.length()-1);
					}
					
				}
				 
				CircleNewsContentMessage video = content.getVideo();
				
				CircleInfo info =	circleService.findCircleInfoByWeChatIdCircleId(circle.getWeChatId(), String.valueOf(circle.getCircleId()));
				if(null != info){
					if(null != video && !StringUtils.isBlank(video.getUrl())){
						info.setVideourl(video.getUrl());
					}
					if(!StringUtils.isBlank(imgUrls)){
						info.setImages(imgUrls);
					}
					if(!StringUtils.isBlank(info.getImages()) || !StringUtils.isBlank(info.getVideourl())){
						circleService.update(info);
					}
				}
			}
			*/
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    
    /**
     * 朋友圈点赞
     * @param req
     */
    @Async
    public  void circleLikeSave(CircleLikeNoticeMessage req){
    	try {
    		if(null == req) {
    			return;
    		}
    		
    		
    		CircleLike like  = circleLikeService.getCircleLikeByFriendid(req.getWeChatId(), String.valueOf(req.getCircleId()), req.getFriendId());
    		if(null != like) {
    			return;
    		}
    		
    		//找到发布者wechatid
    		CircleTaskDetails detail=	 circleTaskService.findByWechatIdMsgId(String.valueOf(req.getCircleId()));
    		
    		CircleLike circleLike = new CircleLike();
    		if(null != detail) {
    			circleLike.setCircle_wechatid(detail.getWechatid());
    		}
    		circleLike.setWechatid(req.getWeChatId());
    		circleLike.setCircleid(String.valueOf(req.getCircleId()));
    		circleLike.setFriendid(req.getFriendId());
    		circleLike.setNickname(req.getNickName());
    		circleLike.setPublishtime(DateUtil.convertDate2String(new Date(req.getPublishTime()*1000), DateUtil.DATE_FORMAT_2));
    		circleLike.setCreateTime(new Date());
    		circleLikeService.insert(circleLike);
    		
    		updateCommentLikeSize(req.getWeChatId(), String.valueOf(req.getCircleId()), true);
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 朋友圈评论
     * @param req
     */
    @Async
    public  void circleCommentSave(CircleCommentNoticeMessage req){
    	try {
    		if(req.getIsDelete()) {
    			CircleComment comment = circleCommentService.getByCircleIdWechatId(String.valueOf(req.getCircleId()), req.getWeChatId());
    			if(null != comment) {
    				circleCommentService.delete(comment);
    			}
    		}else {
    			if(null ==  req.getComment()) {
        			return;
        		}
    			
    			
    			CircleComment cc = circleCommentService.getByCircleIdCommentId(String.valueOf(req.getCircleId()), String.valueOf(req.getComment().getCommentId()));
    			if(null != cc) {
    				return;
    			}
    			
    			//找到发布者wechatid
        		CircleTaskDetails detail =	 circleTaskService.findByWechatIdMsgId(String.valueOf(req.getCircleId()));
        		 
        		CircleComment circleComment = new CircleComment();
        		if(null != detail) {
        			circleComment.setCircle_wechatid(detail.getWechatid());
        			WxAccountInfo  wxAccountInfo = weChatAccountService.findWeChatAccountInfoByWeChatId(detail.getWechatid());
        			if(null != wxAccountInfo) {
        				circleComment.setCid(wxAccountInfo.getCid());
        			}
        		}
        		circleComment.setWechatid(req.getWeChatId());
        		circleComment.setCircleid(String.valueOf(req.getCircleId()));
        		circleComment.setComment(req.getComment().getContent());
        		circleComment.setFromwechatid(req.getComment().getFromWeChatId());
        		circleComment.setFromname(req.getComment().getFromName());
        		circleComment.setTowechatid(req.getComment().getToWeChatId());
        		circleComment.setToname(req.getComment().getToName());
        		circleComment.setCommentid(String.valueOf(req.getComment().getCommentId()));
        		circleComment.setReplycommentid(String.valueOf(req.getComment().getReplyCommentId()));
        		circleComment.setPublishtime(DateUtil.convertDate2String(new Date(req.getComment().getPublishTime()*1000), DateUtil.DATE_FORMAT_2));
        		circleComment.setCreateTime(new Date());
        		circleCommentService.insert(circleComment);
        		
        		
        		updateCommentLikeSize(req.getWeChatId(), String.valueOf(req.getCircleId()), false);
    			
    		}
    		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    
    public void updateCommentLikeSize(String wechatid,String circleid,boolean isLike) {
    	try {
			CircleTaskDetails details=	circleTaskService.findByWechatIdMsgId(wechatid, circleid);
			if(null != details) {
				CircleTask task =	circleTaskService.getByid(details.getTid());
				if(null != task) {
					if(isLike) {
						task.setLikesize(task.getLikesize()+1);
					}else {
						task.setCommentsize(task.getCommentsize()+1);
					}
					circleTaskService.update(task);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    
    
    /**
     * 好友增加通知记录日志
     * @param req
     */
    @Async
    public  void friendAddChangeService(FriendAddNoticeMessage req){
    	try {
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		FriendChange log = new FriendChange();
	    		log.setAccountid(account.getId());
	    		log.setCid(account.getCid());
	    		log.setCreateTime(new Date());
	    		log.setWechatid(req.getWeChatId());
	    		log.setFriendid(req.getFriend().getFriendId());
	    		log.setType(1);//增加好友
	    		friendChangeService.insert(log);	    		
	    	};
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 好友删除通知记录日志
     * @param req
     */
    @Async
    public  void friendDeleteChangeService(FriendDelNoticeMessage req){
    	try {
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		FriendChange log = new FriendChange();
	    		log.setAccountid(account.getId());
	    		log.setCid(account.getCid());
	    		log.setCreateTime(new Date());
	    		log.setWechatid(req.getWeChatId());
	    		log.setFriendid(req.getFriendId());
	    		log.setType(2);//删除好友
	    		friendChangeService.insert(log);	    		
	    	}
    		 
    		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 删除聊天记录日志
     * @param req
     */
    @Async
    public  void deleteMsg(MsgDelNoticeMessage req){
    	try {
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		
	    		WxContactInfo contactinfo = weChatContactService.findContactinfoByfriendid(account.getCid(),req.getWeChatId(), req.getFriendId());
	    		
	    		String content = ByteStringToString.bytesToString(req.getContent(), "utf-8");
	    		MsgDelLog log = new MsgDelLog();
	    		log.setCid(account.getCid());
	    		log.setCreateTime(new Date());
	    		log.setWechatid(req.getWeChatId());
	    		log.setWechatno(account.getWechatno());
	    		log.setNickname(account.getWechatnick());
	    		log.setFriendid(req.getFriendId());
	    		log.setIssend(String.valueOf(req.getIsSend()));
	    		log.setContenttype(req.getContentTypeValue());
	    		log.setContent(content);
	    		log.setMsgid(req.getMsgId()+"");
	    		log.setMsgsvrid(req.getMsgSvrId()+"");
	    		if(null != contactinfo) {
	    			log.setFriendnick(contactinfo.getNickname());
	    		}
	    		msgDelLogService.insert(log);
	    	}
    		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    
    /**
     * 删除聊天会话记录日志
     * @param req
     */
    @Async
    public  void deleteConv(ConvDelNoticeMessage req){
    	try {
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
	    	if(null != account){
	    		  
	    		ConvDelLog log = new ConvDelLog();
	    		log.setCid(account.getCid());
	    		log.setCreateTime(new Date());
	    		log.setWechatid(req.getWeChatId());
	    		log.setWechatno(account.getWechatno());
	    		log.setNickname(account.getWechatnick());
	    		log.setConvid(req.getFriendId());
	    		log.setConvname(req.getConvName());
	    		log.setAvatar(req.getAvatar());
	    		
	    		convDelLogService.insert(log);
	    	}
    		 
    		 
		} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    public void  deleteCircleDelTask(CircleDelNoticeMessage req) {
    	
    }
    
    /**
     * 保存截屏资源
     */
    @Async
    public void saveScreenShotResources(ScreenShotTaskResultNoticeMessage req) {
    	if(null != req && req.getSuccess()) {
    		WxAccountInfo  account = weChatAccountService.findWeChatAccountInfoByWeChatId(req.getWeChatId());
        	if(null != account){
        		Resources info = new Resources();
            	info.setCid(account.getCid());
            	info.setType(100);//5emoji表情素材，4小程序素材，3链接素材，2发朋友圈素材，1群发素材   100截屏素材
            	info.setRemarks("屏幕截图");
            	info.setContent(req.getUrl());
            	info.setCreateTime(new Date());
            	resourcesService.insert(info);
        	}
    	}
    	
    }
    
}
