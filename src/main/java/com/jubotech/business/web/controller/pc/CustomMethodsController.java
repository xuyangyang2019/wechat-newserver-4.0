package com.jubotech.business.web.controller.pc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.CommontagInfo;
import com.jubotech.business.web.domain.CommontermInfo;
import com.jubotech.business.web.domain.FriendAddTask;
import com.jubotech.business.web.domain.FriendAddTaskSetting;
import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.domain.SysAutoSetting;
import com.jubotech.business.web.domain.TaskTimeInfo;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.domain.WxMessageInfo;
import com.jubotech.business.web.domain.vo.AddFriendVo;
import com.jubotech.business.web.domain.vo.PhoneNumberVo;
import com.jubotech.business.web.domain.vo.TaskTimeVo;
import com.jubotech.business.web.query.FriendAddTaskQuery;
import com.jubotech.business.web.query.MessageDetailQuery;
import com.jubotech.business.web.query.MessageQuery;
import com.jubotech.business.web.query.TaskQuery;
import com.jubotech.business.web.service.AccountService;
import com.jubotech.business.web.service.CommontagService;
import com.jubotech.business.web.service.CommontermService;
import com.jubotech.business.web.service.FriendAddTaskService;
import com.jubotech.business.web.service.PhoneNumberService;
import com.jubotech.business.web.service.SysAutoSettingService;
import com.jubotech.business.web.service.TaskTimeService;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.business.web.service.WxMessageService;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.netty.utils.MessageUtil;
import com.jubotech.framework.netty.utils.MsgIdBuilder;
import com.jubotech.framework.netty.utils.NettyConnectionUtil;

import Jubo.JuLiao.IM.Wx.Proto.AddFriendsTask.AddFriendsTaskMessage;
import Jubo.JuLiao.IM.Wx.Proto.TransportMessageOuterClass.EnumMsgType;
import io.netty.channel.ChannelHandlerContext;

@Controller
@CrossOrigin
@RequestMapping("/pc")
public class CustomMethodsController {
	 
	@InitBinder
    public void initListBinder(WebDataBinder binder){
        // ?????????????????????????????????????????????256
		binder.setAutoGrowNestedPaths(true);  
        binder.setAutoGrowCollectionLimit(6000);
    }

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private WxAccountService wxAccountService;

	@Autowired
	private TaskTimeService taskTimeService;

	@Autowired
	private CommontagService commontagService;

	@Autowired
	private CommontermService commontermService;

	@Autowired
	private WxContactService weChatContactService;

	@Autowired
	private WxMessageService weChatMessageService;

	@Autowired
	private SysAutoSettingService sysAutoSettingService;

	@Autowired
	private FriendAddTaskService friendAddTaskService;

	@Autowired
	private PhoneNumberService phoneNumberService;

	@Autowired
	private NettyConnectionUtil nettyConnectionUtil;
	
	
	
	
	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetWeChatsReq")
	@ResponseBody
	public ResultInfo GetWeChatsReq(Integer id) {
		AccountInfo  account = accountService.findAccountInfoByid(id);
		if(null != account){
			List<WxAccountInfo> list =	wxAccountService.findWeChatAccountInfo(account.getCid(), account.getId());
			return ResultInfo.success(list);
		}	
		return ResultInfo.fail();
	}
	
	/**
	 * ?????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetCommonTags")
	@ResponseBody
	public ResultInfo GetCommonTags(CommontagInfo req) {
		Integer id = (int) req.getId();
		String name = req.getName();
		if (!StringUtils.isEmpty(name) && name.equals("pc")) {// ?????????????????????
			AccountInfo account = accountService.findAccountInfoByid(id);
			if (null != account) {
				List<CommontagInfo> list = commontagService.getAllCommontagInfoByCid(account.getCid());
				return ResultInfo.success(list);
			}
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetCommonTerms")
	@ResponseBody
	public ResultInfo GetCommonTerms(CommontermInfo req) {
		Integer id = (int) req.getId();
		String name = req.getName();
		AccountInfo account = accountService.findAccountInfoByid(id);
		if (null != account) {
			List<CommontermInfo> list = commontermService.getAllCommontermInfoByCid(account.getCid(),name);
			return ResultInfo.success(list);
		}
		return ResultInfo.fail();
	}

	/**
	 * ???????????????????????? tag:???????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetWechatFriendList")
	@ResponseBody
	public ResultInfo GetWechatFriendList(WxContactInfo req) {
		if (null != req) {
			List<WxContactInfo> list = weChatContactService.findContactinfoByWeChatId(req.getCid(), req.getWechatid());
			return ResultInfo.success(list);
		}
		return ResultInfo.fail();
	}

	/**
	 * ?????????????????????????????? tag:???????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetWechatFriendDetail")
	@ResponseBody
	public ResultInfo GetWechatFriendDetail(WxContactInfo req) {
		if (null != req) {
			WxContactInfo info = weChatContactService.findContactinfoByfriendid(req.getCid(), req.getWechatid(),
					req.getFriendid());
			return ResultInfo.success(info);
		}
		return ResultInfo.fail();
	}

	/**
	 * ????????????????????????????????? tag:???????????????
	 * 
	 * @return
	 */
	@PostMapping("/QueryHistoryMessage")
	@ResponseBody
	public ResultInfo QueryHistoryMessage(MessageQuery req) {
		if (null != req) {
			PageInfo<WxMessageInfo> list = weChatMessageService.pageList(req);
			return ResultInfo.success(list);
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 */
	@PostMapping("/QueryMessageDetail")
	@ResponseBody
	public ResultInfo QueryMessageDetail(MessageDetailQuery req) {
		if (null != req) { 
		    return ResultInfo.success(weChatMessageService.QueryMessageDetail(req));
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 * 
	 * @param req
	 * @return
	 */
	@PostMapping("/AddTaskTimeInfo")
	@ResponseBody
	public ResultInfo AddTaskTimeInfo(TaskTimeVo req) {
		AccountInfo info = accountService.findAccountInfoByid(req.getAccountid());
		if (null != info) {
			req.setCid(info.getCid());
			return taskTimeService.savePcTask(req);
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetTaskInfoList")
	@ResponseBody
	public ResultInfo GetTaskInfoList(TaskQuery req) {
		if (null != req) {
			PageInfo<TaskTimeInfo> list = taskTimeService.pageList(req);
			return ResultInfo.success(list);
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/TaskStateUpdate")
	@ResponseBody
	public ResultInfo TaskStateUpdate(TaskTimeInfo req) {
		if (null != req) {
			 taskTimeService.taskStateUpdate(req);
			 return ResultInfo.success();
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/TaskDelete")
	@ResponseBody
	public ResultInfo taskDelete(TaskTimeInfo req) {
		if (null != req) {
			 String ids = req.getIds();
			 if(null != ids) {
				 String[] idArray = StringUtils.split(ids, ",");
				 for(int i=0;i<idArray.length;i++) {
					 taskTimeService.delete(Integer.valueOf(idArray[i]));
				 }
			 }
			 return ResultInfo.success();
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/TaskDetail")
	@ResponseBody
	public ResultInfo TaskDetail(TaskTimeInfo req) {
		if (null != req) {
			return taskTimeService.taskDetail(req);
		}
		return ResultInfo.fail();
	}
	

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/AutoTaskDetailList")
	@ResponseBody
	public ResultInfo AutoTaskDetailList(SysAutoSetting req) {
		if (null != req) {
			List<SysAutoSetting> info = sysAutoSettingService.getAllSysAutoSetting(req);
			return ResultInfo.success(info);
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/AutoTaskUpdate")
	@ResponseBody
	public ResultInfo AutoTaskUpdate(SysAutoSetting req) {
		if (null != req) {
			SysAutoSetting info = sysAutoSettingService.insert(req);
			return ResultInfo.success(info);
		}
		return ResultInfo.fail();
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@PostMapping("/AutoFriendAddTask")
	@ResponseBody
	public ResultInfo AutoFriendAddTask(@RequestBody FriendAddTaskSetting req) {
		if (null != req) {
			friendAddTaskService.savePcTask(req);
			return ResultInfo.success();
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ?????????????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/FriendAddTaskStateUpdate")
	@ResponseBody
	public ResultInfo FriendAddTaskStateUpdate(FriendAddTask req) {
		if (null != req) {
			friendAddTaskService.taskStateUpdate(req);
			 return ResultInfo.success();
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/FriendAddTaskDelete")
	@ResponseBody
	public ResultInfo FriendAddTaskDelete(FriendAddTask req) {
		if (null != req) {
			friendAddTaskService.delete(req.getId());
			 return ResultInfo.success();
		}
		return ResultInfo.fail();
	}
	

	/**
	 * ????????????????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetAutoFriendAddTaskList")
	@ResponseBody
	public ResultInfo GetAutoFriendAddTaskList(FriendAddTaskQuery req) {
		if (null != req) {
			Map<String,Object> map = new HashMap<String,Object>();
			PageInfo<FriendAddTask> list = friendAddTaskService.pageList(req);
			Integer count = phoneNumberService.queryNotUsePhoneNumberCount(req.getCid());
			map.put("list", list);
			map.put("count", count);
			return ResultInfo.success(map);
		}
		return ResultInfo.fail();
	}

	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@PostMapping("/GetPhoneNumberList")
	@ResponseBody
	public ResultInfo GetPhoneNumberList(PhoneNumberVo req) {
		if (null != req) {
			List<PhoneNumberInfo> list = phoneNumberService.queryPhoneNumberInfos(req);
			Integer totalSize = phoneNumberService.queryPhoneNumberInfosCount(req);

			// ???????????????(???????????????)
			req.setTask_result(1);
			req.setState(0);
			Integer addFailSize = phoneNumberService.queryPhoneNumberInfosCount(req);

			req.setTask_result(-1);
			req.setState(1);
			Integer notUseSize = phoneNumberService.queryPhoneNumberInfosCount(req);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", list);
			map.put("totalSize", totalSize);
			map.put("addFailSize", addFailSize);
			map.put("notUseSize", notUseSize);

			return ResultInfo.success(map);
		}
		return ResultInfo.fail();
	}
	
	
	/**
	 * ???????????????http??????
	 * 
	 * @return
	 */
	@PostMapping("/friendAdd")
	@ResponseBody
	public ResultInfo friendAdd(AddFriendVo req) {
		if (null != req) {
			try {
				AddFriendsTaskMessage.Builder bd = AddFriendsTaskMessage.newBuilder();
				bd.setTaskId(MsgIdBuilder.getId());
				bd.setWeChatId(req.getWechatId());
				bd.addPhones(req.getPhone());
				bd.setMessage(req.getMessage());
				bd.setRemark(req.getRemark());
				AddFriendsTaskMessage task = bd.build();
				
				ChannelHandlerContext chx = nettyConnectionUtil.getClientChannelHandlerContextByUserId(req.getWechatId());
				if (null != chx) {
					// ???????????????
					MessageUtil.sendMsg(chx, EnumMsgType.AddFriendsTask, null, null, task);
					return ResultInfo.success();
				}else{
					return ResultInfo.fail("???????????????");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResultInfo.fail("????????????");
	}

}
