package com.jubotech.business.web.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.dao.TaskTimeDao;
import com.jubotech.business.web.dao.TimeTaskDetailsDao;
import com.jubotech.business.web.domain.TaskTimeInfo;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.business.web.domain.vo.MessageVo;
import com.jubotech.business.web.domain.vo.TaskTimeVo;
import com.jubotech.business.web.query.TaskQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.util.DateUtil;
import com.jubotech.framework.util.JsonUtils;
import com.jubotech.framework.util.StringUtil;

import Jubo.JuLiao.IM.Wx.Proto.AddFriendsTask.AddFriendsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage.AttachmentMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage.VisibleMessage;
import Jubo.JuLiao.IM.Wx.Proto.PostSNSNewsTask.PostSNSNewsTaskMessage.VisibleMessage.EnumVisibleType;
import Jubo.JuLiao.IM.Wx.Proto.TalkToFriendTask.TalkToFriendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumContentType;
import Jubo.JuLiao.IM.Wx.Proto.WeChatGroupSendTask.WeChatGroupSendTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.WeChatGroupSendTask.WeChatGroupSendTaskMessage.EnumGroupMsgContentType;

@Service
@Transactional // 支持事务
public class TaskTimeService {

	@Autowired
	private TaskTimeDao taskTimeDao;

	@Autowired
	private TimeTaskDetailsDao timeTaskDetailsDao;

	public void deleteTask(String date) {
		try {
			taskTimeDao.deleteTask(date);
			timeTaskDetailsDao.deleteTask(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteTaskDetail(String date) {
		try {
			timeTaskDetailsDao.deleteTask(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PageInfo<TaskTimeInfo> pageList(TaskQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());

//		Example example = new Example(TaskTimeInfo.class);
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("cid", query.getCid());
//		if (null != query.getAccountid() && 0 != query.getAccountid()) {
//			criteria.andEqualTo("accountid", query.getAccountid());
//		}
//		if (null != query.getTasktype() && 0 != query.getTasktype()) {
//			criteria.andEqualTo("tasktype", query.getTasktype());
//		}
//		if (null != query.getState()) {
//			criteria.andEqualTo("state", query.getState());
//		}
//		example.orderBy("id").desc();
//		return new PageInfo<>(taskTimeDao.selectByExample(example));
		
		return new PageInfo<>(taskTimeDao.pageList(query));

	}

	public TaskTimeInfo findTaskTimeInfoByid(Integer id) {
		return taskTimeDao.findTaskTimeInfoByid(id);
	}

	public void delete(Integer id) {
		try {
			// 先删除子任务
			timeTaskDetailsDao.deleteByTid(id);
			// 再删除主任务
			TaskTimeInfo user = new TaskTimeInfo();
			user.setId(id);
			taskTimeDao.delete(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void taskStateUpdate(TaskTimeInfo info) {
		TaskTimeInfo task = findTaskTimeInfoByid(info.getId());
		if (null != task) {
			if(task.getState()==0){
				return;
			}
			
			// 状态 -1暂停 0已完成 1开启中 2取消
			List<TimeTaskDetails> list = timeTaskDetailsDao.findTimeTaskDetailsByTid(info.getId(), 1);// 查询开启中的
			if (null != list && list.size() > 0) {
				stateUpdate(info, list);
			}

			List<TimeTaskDetails> list1 = timeTaskDetailsDao.findTimeTaskDetailsByTid(info.getId(), -1);// 查询暂停的
			if (null != list1 && list1.size() > 0) {
				stateUpdate(info, list1);
			}

			List<TimeTaskDetails> list2 = timeTaskDetailsDao.findTimeTaskDetailsByTid(info.getId(), 2);// 查询取消的
			if (null != list2 && list2.size() > 0) {
				stateUpdate(info, list2);
			}

			task.setState(info.getState());

			try {
				if (Objects.equals(1, info.getState())) {// 开启中
					String dataStr = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
					task.setExecute_time(dataStr);
				}

				taskTimeDao.update(task);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void stateUpdate(TaskTimeInfo info, List<TimeTaskDetails> list) {
		for (TimeTaskDetails detail : list) {
			detail.setState(info.getState());
			try {

				if (Objects.equals(1, info.getState())) {// 开启中
					String str = detail.getExecute_time();
					String[] strs = str.split(" ");
					if (strs.length > 1) {

						String secondStr = strs[1];
						String dataStr = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_1);
						String newTimeStr = dataStr + " " + secondStr;

						detail.setExecute_time(newTimeStr);
					}
				}

				timeTaskDetailsDao.updateState(detail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ResultInfo taskDetail(TaskTimeInfo info) {
		if (null != info && null != info.getId() && info.getId() > 0) {
			// 状态 -1暂停 0已完成 1开启中 2取消
			Integer task_allSize = timeTaskDetailsDao.findTimeTaskDetailsCount(info.getId(), null);
			Integer task_startSize = timeTaskDetailsDao.findTimeTaskDetailsCount(info.getId(), 1);
			Integer task_endSize = timeTaskDetailsDao.findTimeTaskDetailsCount(info.getId(), 0);
			Integer task_stopSize = timeTaskDetailsDao.findTimeTaskDetailsCount(info.getId(), -1);
			Integer task_cancelSize = timeTaskDetailsDao.findTimeTaskDetailsCount(info.getId(), 2);
			Integer task_successSize = timeTaskDetailsDao.findTimeTaskDetailsTaskResultCount(info.getId(), "true");
			Integer task_failSize = timeTaskDetailsDao.findTimeTaskDetailsTaskResultCount(info.getId(), "false");

			Map<String, Object> data = new HashMap<>();
			data.put("task_allSize", task_allSize);// 任务总数
			data.put("task_startSize", task_startSize);// 正在执行的任务数
			data.put("task_endSize", task_endSize);// 已完成的任务数
			data.put("task_stopSize", task_stopSize);// 已暂停的任务数
			data.put("task_cancelSize", task_cancelSize);// 已取消的任务数
			data.put("task_successSize", task_successSize);// 执行成功的任务数
			data.put("task_failSize", task_failSize);// 执行失败的任务数

			return ResultInfo.success(data);
		}
		return ResultInfo.fail();
	}

	public void updateState(TaskTimeInfo info) {
		taskTimeDao.updateState(info);
	}

	public List<TaskTimeInfo> findTaskTimeByTime(String execute_time) {
		return taskTimeDao.findTaskTimeByTime(execute_time);
	}

	public List<TimeTaskDetails> findTimeTaskDetailsByTid(Integer tid) {
		return timeTaskDetailsDao.findTimeTaskDetailsByTid(tid, 1);
	}

	public List<TimeTaskDetails> findTimeTaskDetailsByTidDate(Integer tid, Integer state,String date) {
		return timeTaskDetailsDao.findTimeTaskDetailsByTidDate(tid, state,date);
	}

	private static void saveCircleSendTimeTaskDetail(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao) {
		if (null != info.getId()) {

			String wechatId = info.getWechatId();
			List<String> friendWechatList = StringUtil.stringToList(info.getWhoinvisible());

			List<String> newfriendWechatList = null;
			if (null != friendWechatList && friendWechatList.size() > 0) {
				// friendWechatList去除重复
				newfriendWechatList = StringUtil.removeRepeat(friendWechatList);
			}

			long msgId = MsgIdBuilder.getId();

			TimeTaskDetails detail = new TimeTaskDetails();
			detail.setTid(info.getId());
			detail.setMsgid(String.valueOf(msgId));
			detail.setExecute_time(info.getExecute_time());
			timeTaskDetailsDao.insert(detail);

			if (null != detail.getId()) {
				
				if(info.getReplace().equalsIgnoreCase("true")) {
					String content= info.getContent();
					content=StringUtil.getEmoji()+content+StringUtil.getEmoji();
					info.setContent(content);
				}
				
				List<String>  comments = StringUtil.stringToList(info.getComment(), "###");//多个评论
				
				// 设置附件
				AttachmentMessage.Builder attachment = AttachmentMessage.newBuilder();
				attachment.setTypeValue(info.getRestype());
				attachment.addAllContent(StringUtil.stringToList(info.getAttachtcontent()));
				// 设置可见范围
				VisibleMessage.Builder visible = VisibleMessage.newBuilder();
				visible.setTypeValue(EnumVisibleType.WhoInvisible_VALUE);// 不给谁看
				visible.setFriends(StringUtil.ListToString(newfriendWechatList));

				// 按微信号生成需要发的消息内容
				PostSNSNewsTaskMessage.Builder buider = PostSNSNewsTaskMessage.newBuilder();
				buider.setContent(info.getContent());
				buider.setAttachment(attachment);
				buider.setTaskId(msgId);
				buider.setWeChatId(wechatId);
				//buider.setComment(info.getComment());
				buider.addAllExtComment(comments);
				buider.setVisible(visible);
				PostSNSNewsTaskMessage msg = buider.build();
				String json = null;
				if (null != msg) {
					try {
						json = JsonFormat.printer().print(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (!StringUtils.isEmpty(json)) {
					TimeTaskDetails tds = new TimeTaskDetails();
					tds.setId(detail.getId());
					tds.setJson_content(json);
					timeTaskDetailsDao.updateJsonContent(tds);
				}
			}
		}

	}

	private static void saveGroupSendTimeTaskDetail(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao) {
		if (null != info.getId()) {
			List<String> wechatList = info.getWechatList();
			if (null == wechatList || wechatList.size() == 0) {
				return;
			}
			wechatList = StringUtil.removeRepeat(wechatList);// 去除重复
			talkMsgToFriend(info, timeTaskDetailsDao, wechatList);
		}

	}

	private static void saveGroupSendQunTaskDetail(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao) {
		if (null != info.getId()) {
			List<String> friends = info.getWechatList();
			if (null != friends && friends.size() > 0) {
				talkMsgToFriend(info, timeTaskDetailsDao, friends);
			}
		}
	}

	@SuppressWarnings("unused")
	private static void talkMsgToFriendByHelper(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao,
			List<String> friends) {
		WeChatGroupSendTaskMessage.Builder build = WeChatGroupSendTaskMessage.newBuilder();
		if (info.getRestype() == 1) {// 文本类型
			build.setContentType(EnumGroupMsgContentType.Text);
		} else {// 图片类型
			build.setContentType(EnumGroupMsgContentType.Picture);
		}
		build.setContent(info.getContent());
		build.setTaskId(MsgIdBuilder.getId());
		build.setWeChatId(info.getWechatId());
		build.addAllFriendIds(friends);
		WeChatGroupSendTaskMessage msg = build.build();
		if (null != msg) {
			try {
				String json = JsonFormat.printer().print(msg);
				if (!StringUtils.isEmpty(json)) {
					TimeTaskDetails detail = new TimeTaskDetails();
					detail.setTid(info.getId());
					detail.setExecute_time(info.getExecute_time());
					detail.setJson_content(json);
					timeTaskDetailsDao.insert(detail);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void talkMsgToFriend(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao,
			List<String> friends) {
		String executeTime = info.getExecute_time();
		Date newDate = DateUtil.convertString2Date(executeTime, DateUtil.DATE_FORMAT_4);
		Integer betweenTime = info.getBetween_time();
		
		if (betweenTime ==null || betweenTime==0) {
			betweenTime = (int)(Math.random()*3+3);
		}

		for (String friend : friends) {

			String newExecuteTime = DateUtil.convertDate2String(newDate, DateUtil.DATE_FORMAT_4);
			String msgStr = "";
			String jsonStr = info.getContent();
			List<MessageVo> list = JsonUtils.json2Bean(jsonStr);

			for (int i = 0; i < list.size(); i++) {
				MessageVo msg = list.get(i);

				ByteString byteString = ByteString.copyFromUtf8(msg.getContent());

				TalkToFriendTaskMessage.Builder build = TalkToFriendTaskMessage.newBuilder();
				build.setWeChatId(info.getWechatId());
				build.setFriendId(friend);
				build.setContent(byteString);
				build.setMsgId(MsgIdBuilder.getId());

				if (msg.getContentType().equalsIgnoreCase("Text")) {// 文本类型
					build.setContentType(EnumContentType.Text);
				} else if (msg.getContentType().equalsIgnoreCase("Picture")) {// 图片类型
					build.setContentType(EnumContentType.Picture);
				} else if (msg.getContentType().equalsIgnoreCase("Voice")) {// 语音类型
					build.setContentType(EnumContentType.Voice);
				} else if (msg.getContentType().equalsIgnoreCase("Video")) {// 视频类型
					build.setContentType(EnumContentType.Video);
				} else if (msg.getContentType().equalsIgnoreCase("Link")) {// 链接类型
					build.setContentType(EnumContentType.Link);
				} else if (msg.getContentType().equalsIgnoreCase("WeApp")) {// 小程序
					build.setContentType(EnumContentType.WeApp);
				}

				TalkToFriendTaskMessage msg1 = build.build();
				if (null != msg1) {
					try {
						String json = JsonFormat.printer().print(msg1);

						if (!StringUtils.isEmpty(json)) {
							msgStr = msgStr + json;
							if (i != (list.size() - 1)) {
								msgStr = msgStr + "&&";
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

			if (null != msgStr) {
				try {
					if (!StringUtils.isEmpty(msgStr)) {
						TimeTaskDetails detail = new TimeTaskDetails();
						detail.setTid(info.getId());
						detail.setExecute_time(newExecuteTime);
						detail.setJson_content(msgStr);
						detail.setMsgid(String.valueOf(MsgIdBuilder.getId()));
						timeTaskDetailsDao.insert(detail);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			newDate = DateUtil.offsetSecode(newDate, betweenTime);

		}
	}

	private static void saveAddFriendsTimeTaskDetail(TaskTimeInfo info, TimeTaskDetailsDao timeTaskDetailsDao) {
		if (null != info.getId()) {

			Date newDate = DateUtil.convertString2Date(info.getExecute_time(), DateUtil.DATE_FORMAT_4);
			Integer betweenTime = 30;
			if (info.getBetween_time() != null) {
				betweenTime = info.getBetween_time();
			}

			List<String> friendAddList = info.getWechatList();

			if (null != friendAddList && friendAddList.size() > 0) {
				for (String phone : friendAddList) {
					AddFriendsTaskMessage.Builder bd = AddFriendsTaskMessage.newBuilder();
					bd.setWeChatId(info.getWechatId());
					bd.addPhones(phone);
					bd.setTaskId(MsgIdBuilder.getId());
					bd.setMessage(info.getMessage());
					bd.setRemark(info.getRemarks());
					AddFriendsTaskMessage msg = bd.build();

					if (null != msg) {
						try {
							String json = JsonFormat.printer().print(msg);
							if (!StringUtils.isEmpty(json)) {
								TimeTaskDetails detail = new TimeTaskDetails();
								detail.setTid(info.getId());
								detail.setExecute_time(DateUtil.convertDate2String(newDate, DateUtil.DATE_FORMAT_2));
								detail.setJson_content(json);
								timeTaskDetailsDao.insert(detail);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					newDate = DateUtil.offsetSecode(newDate, betweenTime);
				}
			}
		}

	}

	/**
	 * pc端添加定时任务
	 * 
	 * @param info
	 * @return
	 */
	public ResultInfo savePcTask(TaskTimeVo req) {
		ResultInfo res = ResultInfo.success();
		try {

			TaskTimeInfo info = new TaskTimeInfo();

			BeanUtils.copyProperties(req, info);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("attachtype", req.getAttachtype());
			data.put("attachtcontent", req.getAttachtcontent());
			data.put("whoinvisible", req.getWhoinvisible());
			String datastr = JSON.toJSONString(data);
			info.setRemark2(datastr);
			
			if(org.apache.commons.lang3.StringUtils.isEmpty(req.getRemarks())) {
				info.setRemarks(DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_6));
			}

			// 主任务存储数据库
			info.setState(1);// 1开启中 0已完成
			taskTimeDao.insert(info);

			// 子任务分别处理 存储子任务详情
			if (info.getTasktype() == 1) {// 添加群发任务
				saveGroupSendTimeTaskDetail(info, timeTaskDetailsDao);
			} else if (info.getTasktype() == 2) {// 发朋友圈
				saveCircleSendTimeTaskDetail(info, timeTaskDetailsDao);
			} else if (info.getTasktype() == 5) {// 群发群
				saveGroupSendQunTaskDetail(info, timeTaskDetailsDao);
			} else if (info.getTasktype() == 6) {// 添加好友
				saveAddFriendsTimeTaskDetail(info, timeTaskDetailsDao);
			}

		} catch (Exception e) {
			res = ResultInfo.fail();
			e.printStackTrace();
		}

		return res;
	}

	public static void main(String[] args) {
		System.out.println((int)(Math.random()*3));
	}
}
