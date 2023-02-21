package com.jubotech.framework.netty.utils;

import java.util.Random;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.domain.FriendAddTaskDetails;
import com.jubotech.business.web.domain.TaskTimeInfo;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.framework.proxy.ProxyUtil;

import Jubo.JuLiao.IM.Wx.Proto.AddFriendsTask.AddFriendsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.CircleCommentDeleteTask.CircleCommentDeleteTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.DeleteSNSNewsTask.DeleteSNSNewsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.OneKeyLikeTask.OneKeyLikeTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TalkToFriendTask.TalkToFriendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import Jubo.JuLiao.IM.Wx.Proto.WeChatGroupSendTask.WeChatGroupSendTaskMessage;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonToProtoConverterUtil {

	static NettyConnectionUtil nettyConnectionUtil = ProxyUtil.getBean(NettyConnectionUtil.class);

	public static boolean sendProtoMsg(TaskTimeInfo task, TimeTaskDetails details) {
		if (task.getTasktype() == 1) {// 群发好友
			return talkToFriendTaskMessage(details);
		} else if (task.getTasktype() == 2) {
			return sendPostSNSNewsTaskMessage(details.getJson_content());
		} else if (task.getTasktype() == 5) {// 群发群
			return talkToFriendTaskMessage(details);
		} else if (task.getTasktype() == 6) {// 加好友
			return sendAddFriendTaskMessage(details);
		}
		return false;
	}

	public static boolean sendAddFriendTaskMessage(TimeTaskDetails details) {
		try {
			AddFriendsTaskMessage.Builder bd = AddFriendsTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			AddFriendsTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.AddFriendsTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"加好友未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendAddFriendsTaskMessage(FriendAddTaskDetails details) {
		try {
			AddFriendsTaskMessage.Builder bd = AddFriendsTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			AddFriendsTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.AddFriendsTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"sop自动加好友未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
 

	public static boolean deleteSNSNewsTaskMessage(String circleid, String wechatid) {
		try {
			DeleteSNSNewsTaskMessage.Builder bd = DeleteSNSNewsTaskMessage.newBuilder();
			bd.setCircleId(Long.valueOf(circleid));
			bd.setWeChatId(wechatid);
			bd.setTaskId(MsgIdBuilder.getId());
			DeleteSNSNewsTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.DeleteSNSNewsTask, null, null, req);

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(new Random().nextInt(1500));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						MessageUtil.sendMsg(chx, EnumMsgType.DeleteSNSNewsTask, null, null, req);
					}
				}).start();

				return true;
			}
			
			log.info(req.getWeChatId()+"删除朋友圈未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteSNSNewsTaskMessage(TimeTaskDetails details) {
		try {
			DeleteSNSNewsTaskMessage.Builder bd = DeleteSNSNewsTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			DeleteSNSNewsTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.DeleteSNSNewsTask, null, null, req);

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(new Random().nextInt(1500));
							MessageUtil.sendMsg(chx, EnumMsgType.DeleteSNSNewsTask, null, null, req);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();

				return true;
			}
			
			log.info(req.getWeChatId()+"定时任务删除朋友圈未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteCircleCommentMessage(String circleid, String commentid, String wechatid) {
		try {
			CircleCommentDeleteTaskMessage.Builder bd = CircleCommentDeleteTaskMessage.newBuilder();
			bd.setCircleId(Long.valueOf(circleid));
			bd.setCommentId(Long.valueOf(commentid));
			bd.setWeChatId(wechatid);
			bd.setTaskId(MsgIdBuilder.getId());
			CircleCommentDeleteTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.CircleCommentDeleteTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"删除评论未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteCircleCommentMessage(TimeTaskDetails details) {
		try {
			CircleCommentDeleteTaskMessage.Builder bd = CircleCommentDeleteTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			CircleCommentDeleteTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.CircleCommentDeleteTask, null, null, req);

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(new Random().nextInt(1500));
							MessageUtil.sendMsg(chx, EnumMsgType.CircleCommentDeleteTask, null, null, req);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();

				return true;
			}
			log.info(req.getWeChatId()+"定时任务删除评论未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendPostSNSNewsTaskMessage(String json_content) {
		try {
			PostSNSNewsTaskMessage.Builder bd = PostSNSNewsTaskMessage.newBuilder();
			JsonFormat.parser().merge(json_content, bd);
			PostSNSNewsTaskMessage req = bd.build();

			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.PostSNSNewsTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"发朋友圈未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean sendWeChatGroupSendTaskMessage(TimeTaskDetails details) {
		try {
			WeChatGroupSendTaskMessage.Builder bd = WeChatGroupSendTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			WeChatGroupSendTaskMessage req = bd.build();
			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.WeChatGroupSendTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"定时发朋友圈未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	private static boolean talkToFriendTaskMessage(TimeTaskDetails details) {
		try {
			String constr = details.getJson_content();
			boolean flag = false;
			String[] strs = constr.split("&&");
			if (null != strs && strs.length > 0) {
				for (int i = 0; i < strs.length; i++) {
					TalkToFriendTaskMessage.Builder bd = TalkToFriendTaskMessage.newBuilder();
					JsonFormat.parser().merge(strs[i], bd);
					TalkToFriendTaskMessage req = bd.build();
					ChannelHandlerContext chx = nettyConnectionUtil
							.getClientChannelHandlerContextByUserId(req.getWeChatId());
					if (null != chx) {
						// 发给手机端
						MessageUtil.sendMsg(chx, EnumMsgType.TalkToFriendTask, null, null, req);
						flag = true;
					}
					log.info(req.getWeChatId()+"定时群发未找到通道");
				}
			}
			return flag;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendOneKeyLikeTaskMessage(TimeTaskDetails details) {
		try {
			OneKeyLikeTaskMessage.Builder bd = OneKeyLikeTaskMessage.newBuilder();
			JsonFormat.parser().merge(details.getJson_content(), bd);
			OneKeyLikeTaskMessage req = bd.build();
			ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWeChatId());
			if (null != chx) {
				// 发给手机端
				MessageUtil.sendMsg(chx, EnumMsgType.OneKeyLikeTask, null, null, req);
				return true;
			}
			log.info(req.getWeChatId()+"定时点赞未找到通道");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
