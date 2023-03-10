package com.jubotech.framework.config;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.protobuf.util.JsonFormat;
import com.jubotech.business.web.domain.CircleTask;
import com.jubotech.business.web.domain.CircleTaskDetails;
import com.jubotech.business.web.domain.FriendAddLog;
import com.jubotech.business.web.domain.FriendAddTask;
import com.jubotech.business.web.domain.FriendAddTaskDetails;
import com.jubotech.business.web.domain.SysAutoSetting;
import com.jubotech.business.web.domain.TaskTimeInfo;
import com.jubotech.business.web.domain.TimeTaskDetails;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.service.CircleTaskService;
import com.jubotech.business.web.service.FriendAddLogService;
import com.jubotech.business.web.service.FriendAddTaskService;
import com.jubotech.business.web.service.SysAutoSettingService;
import com.jubotech.business.web.service.TaskTimeService;
import com.jubotech.business.web.service.TimeTaskDetailsService;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.framework.common.AutoType;
import com.jubotech.framework.netty.utils.JsonToProtoConverterUtil;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;
import com.jubotech.framework.util.DateUtil;

import Jubo.JuLiao.IM.Wx.Proto.OneKeyLikeTask.OneKeyLikeTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.PullFriendAddReqListTask.PullFriendAddReqListTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Service
@EnableScheduling
@EnableAsync
@Slf4j
public class TimingTaskService {

	@Autowired
	private EhcacheService ehcacheService;

	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;

	@Autowired
	private TaskTimeService taskTimeService;

	@Autowired
	private TimeTaskDetailsService timeTaskDetailsService;

	@Autowired
	private CircleTaskService circleTaskService;

	@Autowired
	private FriendAddTaskService friendAddTaskService;

	@Autowired
	private SysAutoSettingService sysAutoSettingService;
	
	@Autowired
	private  WxAccountService wxAccountService;
	
	@Autowired
	private  WxContactService wxContactService;
	
	
	@Autowired
	private  FriendAddLogService friendAddLogService;
	
	/**
	 * ?????????????????????
	 */
	@Async
	@Scheduled(cron = "0 0 0/1 * * ?") // ??????
	public void executeHourTask() {
		try {
			
		 List<WxAccountInfo>  list = 	wxAccountService.findAllAccountWechatInfo();
		 if(null != list && list.size()>0) {
			 Date now = new Date();
			 Date start=DateUtil.offsetHour(now, -1);
			 Date end=now;
			 for(WxAccountInfo wxAccountInfo:list) {
				 Integer count =  wxContactService.queryFriendAddCount(wxAccountInfo.getWechatid(), start, end);
				 
				 FriendAddLog log = new FriendAddLog();
				 log.setCid(wxAccountInfo.getCid());
				 log.setCount(count);
				 log.setCreateTime(now);
				 log.setGroupid(wxAccountInfo.getGroupid());
				 log.setWechatid(wxAccountInfo.getWechatid());
				 log.setNickname(wxAccountInfo.getWechatnick());
				 log.setSnumber(wxAccountInfo.getSnumber());
				 
				 friendAddLogService.insert(log);
			 }
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ???10????????????????????????????????????????????????
	 */
	@Async
	@Scheduled(cron = "0 0/10 8-22 * * ?") // ??????
	public void executeTaskOfFriendAdd() {
		try {
			Map<String, ChannelHandlerContext> map = NettyConnectionUtil.userId_nettyChannel;
			if (null != map && map.size() > 0) {
				for (Map.Entry<String, ChannelHandlerContext> entry : map.entrySet()) {
					String key = entry.getKey();
					SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(key,AutoType.AUTOTYPE_FRIENDREQEST);
					if (null != sys && sys.getState()==0){
						PullFriendAddReqListTaskMessage.Builder bd = PullFriendAddReqListTaskMessage.newBuilder();
						bd.setWeChatId(key);
						bd.setStartTime(System.currentTimeMillis() - (24 * 60 * 60 * 1000));
						PullFriendAddReqListTaskMessage msg = bd.build();
						MessageUtil.sendMsg(entry.getValue(), EnumMsgType.PullFriendAddReqListTask, null, null, msg);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ??????????????????????????????????????????????????????
	 */
	@Async
	@Scheduled(cron = "0 0/1 * * * ?") // ??????
	public void executeTask() {
		try {
			checkTask();// ????????????????????????
			sendCircleTask();// ????????????????????????
			deleteCircleTask();// ?????????????????????
			deleteCircleCommentTask();// ???????????????????????????
			checkAddFriendTask();// SOP???????????????????????????
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Async
	public void checkAddFriendTask() {// ?????????????????????
		try {
			log.info(LocalDateTime.now() + " ???????????????????????????  ??????????????????: " + Thread.currentThread().getName());
			String execute_time = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
			 
			// ???????????????
			List<FriendAddTaskDetails> tasks1 = friendAddTaskService.findFriendAddTaskDetailsByTime(execute_time);
			if (null != tasks1 && tasks1.size() > 0) {
				for (int i = 0; i < tasks1.size(); i++) {
					FriendAddTaskDetails task = tasks1.get(i);
					if (null != task) {
						 
						boolean flag = JsonToProtoConverterUtil.sendAddFriendsTaskMessage(task);
						if (flag) {
							task.setState(0);// ?????????????????????
							friendAddTaskService.updateDetailState(task);
							//friendAddTaskService.delete(task);

							// ???????????????
							FriendAddTask faddTask = friendAddTaskService.findFriendAddTaskByid(task.getTid());
							if (null != faddTask) {
								Integer doingsize = faddTask.getDoingsize() + 1;
								faddTask.setDoingsize(doingsize);
								friendAddTaskService.update(faddTask);
							}
						}
						 
					}
				}
			}

			// ???????????????
			List<FriendAddTask> tasks = friendAddTaskService.findFriendAddTaskByTime(execute_time);
			if (null != tasks && tasks.size() > 0) {
				// ????????????????????????
				Date tomorrow = DateUtil.offsetDay(new Date(), 1);
				execute_time = DateUtil.convertDate2String(tomorrow, DateUtil.DATE_FORMAT_4);
				for (int i = 0; i < tasks.size(); i++) {
					FriendAddTask task = tasks.get(i);
					if (null != task) {
						try {
							// ???????????????
							boolean flag = friendAddTaskService.createTaskDetails(task);
							// ????????????????????????
							if (flag) {
								task.setExecute_time(execute_time);
							} else {
								task.setState(0);
							}
							friendAddTaskService.update(task);
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
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
	 * ????????????????????????
	 */
	@Async
	public synchronized void  checkTask() {
		try {
			log.info(LocalDateTime.now() + " ????????????????????????  ??????????????????: " + Thread.currentThread().getName());
 
			String execute_time = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
			List<TaskTimeInfo> tasks = taskTimeService.findTaskTimeByTime(execute_time);
			if (null != tasks && tasks.size() > 0) {
				for (int i = 0; i < tasks.size(); i++) {
					TaskTimeInfo task = tasks.get(i);
					if (null != task) {
						List<TimeTaskDetails> taskDetails = taskTimeService.findTimeTaskDetailsByTidDate(task.getId(),1,execute_time);
						if (null != taskDetails && taskDetails.size() > 0) {
							for (int j = 0; j < taskDetails.size(); j++) {
								TimeTaskDetails details = taskDetails.get(j);
								
								boolean flag = JsonToProtoConverterUtil.sendProtoMsg(task, taskDetails.get(j));
								if (flag) {
									details.setState(0);// ?????????????????????
									timeTaskDetailsService.updateState(details);
									//timeTaskDetailsService.delete(details);//????????????
								}
								 
							}
						} 
						 
						List<TimeTaskDetails> taskDetails1 = taskTimeService.findTimeTaskDetailsByTid(task.getId());
						if(null == taskDetails1 || taskDetails1.size()==0) {
							task.setState(0);// ?????????????????????
							taskTimeService.updateState(task);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		 

	}

	/**
	 * ??????????????????????????????
	 */
	@Async
	public void sendCircleTask() {
		try {
			log.info(LocalDateTime.now() + " ??????????????????????????????  ??????????????????: " + Thread.currentThread().getName());

			String execute_time = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
			List<CircleTask> tasks = circleTaskService.findTaskTimeByTime(execute_time);
			if (null != tasks && tasks.size() > 0) {
				for (int i = 0; i < tasks.size(); i++) {
					CircleTask task = tasks.get(i);
					if (null != task) {
						List<CircleTaskDetails> taskDetails = circleTaskService.findTimeTaskDetailsByTid(task.getId(),execute_time);
						if (null != taskDetails && taskDetails.size() > 0) {
							for (int j = 0; j < taskDetails.size(); j++) {
								CircleTaskDetails details = taskDetails.get(j);
							 
								boolean flag = JsonToProtoConverterUtil.sendPostSNSNewsTaskMessage(taskDetails.get(j).getJson_content());
								if (flag) {
									details.setState(0);// ?????????????????????
									circleTaskService.updateDetail(details);
								}
								 
							}
						} 
						
						List<CircleTaskDetails> taskDetails1 = circleTaskService.findTimeTaskDetailsByTidState(task.getId(),1);
						if(null == taskDetails1 || taskDetails1.size()==0) {
							task.setState(0);// ?????????????????????
							circleTaskService.update(task);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ???????????????????????????
	 */
	@Async
	public void deleteCircleTask() {
		try {
			log.info(LocalDateTime.now() + " ???????????????????????????  ??????????????????: " + Thread.currentThread().getName());

			String execute_time = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
			 
			List<TimeTaskDetails> taskDetails = taskTimeService.findTimeTaskDetailsByTidDate(0, 1,execute_time);
			if (null != taskDetails && taskDetails.size() > 0) {
				for (int j = 0; j < taskDetails.size(); j++) {
					TimeTaskDetails details = taskDetails.get(j);
					boolean flag = JsonToProtoConverterUtil.deleteSNSNewsTaskMessage(taskDetails.get(j));
					if (flag) {
						timeTaskDetailsService.delete(details);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ?????????????????????????????????
	 */
	@Async
	public void deleteCircleCommentTask() {
		try {
			log.info(LocalDateTime.now() + " ?????????????????????????????????  ??????????????????: " + Thread.currentThread().getName());

			String execute_time = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_4);
			 
			List<TimeTaskDetails> taskDetails = taskTimeService.findTimeTaskDetailsByTidDate(-1, 1,execute_time);
			if (null != taskDetails && taskDetails.size() > 0) {
				for (int j = 0; j < taskDetails.size(); j++) {
					TimeTaskDetails details = taskDetails.get(j);
					 
					boolean flag = JsonToProtoConverterUtil.deleteCircleCommentMessage(taskDetails.get(j));
					if (flag) {
						timeTaskDetailsService.delete(details);
					}
					 
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ???30???????????????????????????
	 */
	@Async
	@Scheduled(cron = "0/59 * * * * ?") // ??????
	public void checkChannel() {
		try {

			log.info(LocalDateTime.now() + " ???30???????????????????????????????????????  ??????????????????: " + Thread.currentThread().getName());

			Map<String, ChannelHandlerContext> map = NettyConnectionUtil.userId_nettyChannel;
			if (null != map && map.size() > 0) {
				Cache cache = ehcacheService.getCache();
				for (Map.Entry<String, ChannelHandlerContext> entry : map.entrySet()) {
					try {
						ChannelHandlerContext ctx = entry.getValue();
						String key = nettyConnectionUtil.getNettyId(ctx);
						if (null != cache) {
							Integer value = 0;
							if (null != cache.get(key)) {
								value = cache.get(key, Integer.class);
								if (value > 3) {
									nettyConnectionUtil.exit(ctx);
									value = 0;
								} else {
									value = value + 1;
								}
							}
							cache.put(key, value);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@Async
	//@Scheduled(cron = "0 0/1 22 * * ?") 
	public void doMomentsPraise() {
		try {
			log.info(LocalDateTime.now() + " ??????????????????  ??????????????????: " + Thread.currentThread().getName());
			long execute_long = new Date().getTime();
			List<TimeTaskDetails> details = timeTaskDetailsService.findTimeTaskDetailsByTid(-2);
			if (null != details) {
				for (TimeTaskDetails detail : details) {
					long details_execute_long = DateUtil
							.convertString2Date(detail.getExecute_time(), DateUtil.DATE_FORMAT_4).getTime();
					if (execute_long >= details_execute_long) {
						boolean flag = JsonToProtoConverterUtil.sendOneKeyLikeTaskMessage(detail);
						if(flag) {
							try {
								timeTaskDetailsService.delete(detail);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@Async
	//@Scheduled(cron = "0 50 21 * * ?") 
	public void checkMomentsPraise() {
		try {

			log.info(LocalDateTime.now() + " ????????????????????????  ??????????????????: " + Thread.currentThread().getName());
			try {
				timeTaskDetailsService.deleteByTid(-2);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			Map<String, ChannelHandlerContext> map = NettyConnectionUtil.userId_nettyChannel;
			if (null != map && map.size() > 0) {
				for (Map.Entry<String, ChannelHandlerContext> entry : map.entrySet()) {
					String key = entry.getKey();
					SysAutoSetting sys = sysAutoSettingService.findSettingByWcIdAutoType(key,
							AutoType.AUTOTYPE_MOMENTSPRAISE);
					if (null != sys) {
						OneKeyLikeTaskMessage.Builder bd = OneKeyLikeTaskMessage.newBuilder();
						bd.setWeChatId(key);
						bd.setTaskId(MsgIdBuilder.getId());
						bd.setRate(new Random().nextInt(30) + 30);
						OneKeyLikeTaskMessage msg = bd.build();
						// MessageUtil.sendMsg(ctx, EnumMsgType.OneKeyLikeTask,null,null, msg);

						if (null != msg) {
							try {
								String json = JsonFormat.printer().print(msg);
								if (!StringUtils.isEmpty(json)) {
									TimeTaskDetails detail = new TimeTaskDetails();
									detail.setTid(-2);
									detail.setState(1);
									detail.setExecute_time(getOneKeyLikeDate());
									detail.setJson_content(json);
									timeTaskDetailsService.insert(detail);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}
				}
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * ??????????????????4?????????????????????
	 */
	@Async
	@Scheduled(cron = "1 0 0 * * ?") 
	public void deleteTaskDataByDay() {
		try {

			log.info(LocalDateTime.now() + " ????????????4?????????????????????  ??????????????????: " + Thread.currentThread().getName());

			Date date = DateUtil.offsetDay(new Date(), -4);
			String dateStr = DateUtil.convertDate2String(date, DateUtil.DATE_FORMAT_2);

			taskTimeService.deleteTaskDetail(dateStr);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * ??????????????????????????????
	 */
	@Async
	//@Scheduled(cron = "0 0 0 1 * ?") //
	public void deleteTaskData() {
		try {

			log.info(LocalDateTime.now() + " ????????????????????????  ??????????????????: " + Thread.currentThread().getName());

			Date date = DateUtil.offsetMonth(new Date(), -3);
			String dateStr = DateUtil.convertDate2String(date, DateUtil.DATE_FORMAT_2);

			taskTimeService.deleteTask(dateStr);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static String getOneKeyLikeDate() {
		String ymd = DateUtil.convertDate2String(new Date(), DateUtil.DATE_FORMAT_1);
		Integer mm = new Random().nextInt(59);
		String mmStr = "";
		if (mm < 10) {
			mmStr = "0" + mm;
		} else {
			mmStr = mm + "";
		}
		return ymd + " " + "22:" + mmStr;
	}

}
