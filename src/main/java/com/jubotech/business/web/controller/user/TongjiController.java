package com.jubotech.business.web.controller.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.FriendCountData;
import com.jubotech.business.web.domain.Tongji;
import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.domain.vo.FriendChangeVo;
import com.jubotech.business.web.domain.vo.FriendVo;
import com.jubotech.business.web.domain.vo.LuckyMoneyDetail;
import com.jubotech.business.web.domain.vo.LuckyMoneyTongji;
import com.jubotech.business.web.query.FriendAddLogQuery;
import com.jubotech.business.web.query.FriendChangeQuery;
import com.jubotech.business.web.query.LuckyMoneyQuery;
import com.jubotech.business.web.service.CircleTaskService;
import com.jubotech.business.web.service.FriendAddLogService;
import com.jubotech.business.web.service.FriendChangeService;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.business.web.service.WxLuckymoneyService;
import com.jubotech.business.web.service.WxMessageService;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.util.DateUtil;

@Controller
@RequestMapping("/user/data")
public class TongjiController {
	  
	@Autowired
	private WxAccountService wxAccountService;
	
	@Autowired
	private WxContactService wxContactService;
	
	@Autowired
	private WxMessageService wxMessageService;
	
	@Autowired
	private CircleTaskService circleTaskService;
	
	@Autowired
	private FriendAddLogService friendAddLogService;
	
	@Autowired
	private WxLuckymoneyService wxLuckymoneyService;
	
	@Autowired
	private FriendChangeService friendChangeService;
  
	  
	@PostMapping("/tongji")
	@ResponseBody
	public ResultInfo tongji(Integer cid) {
		Integer  wechatAllSize = wxAccountService.findAllWeChatAccount(cid);
		Integer  onLineWeChatSize = wxAccountService.findOnLineWeChatAccount(cid);
		Integer weChatContactSize = wxContactService.findContactinfoByCidType(cid, 0);
		Integer qunContactSize = wxContactService.findContactinfoByCidType(cid, 1);
		Integer weChatMessageSize= wxMessageService.queryWeChatMessageCountByCidType(cid, 0);
		Integer qunMessageSize= wxMessageService.queryWeChatMessageCountByCidType(cid, 1);
		Map<String,Object> data= new HashMap<>();
		data.put("wechatAllSize",wechatAllSize);
		data.put("onLineWeChatSize",onLineWeChatSize);
		data.put("weChatContactSize",weChatContactSize);
		data.put("qunContactSize",qunContactSize);
		data.put("weChatMessageSize",weChatMessageSize);
		data.put("qunMessageSize",qunMessageSize);
		 						  
		return ResultInfo.success(data);
	}
	
	@PostMapping("/baobiao")
	@ResponseBody
	public ResultInfo baobiao(@RequestParam(value = "cid") Integer cid,
			@RequestParam(value = "start") String start,@RequestParam(value = "end") String end) {
		
		if(cid == null ||  StringUtils.isEmpty(start) || StringUtils.isEmpty(end)){
			return ResultInfo.fail("参数传入错误");
		}
		 
		start = DateUtil.convertDate2String(new Date(Long.valueOf(start)), DateUtil.DATE_FORMAT_1);
		end = DateUtil.convertDate2String(new Date(Long.valueOf(end)), DateUtil.DATE_FORMAT_1);
		
		Date startd = DateUtil.convertString2Date(start, DateUtil.DATE_FORMAT_1);
		Date endd = DateUtil.convertString2Date(end, DateUtil.DATE_FORMAT_1);
		 
		List<Tongji> chatMsgSendSize = wxMessageService.queryTongji(cid,0,"true",startd,endd);//个人聊天发送消息
		List<Tongji> chatMsgReceivedSize = wxMessageService.queryTongji(cid,0,"false",startd,endd);//个人聊天收到消息
		List<Tongji> qunMsgSendSize = wxMessageService.queryTongji(cid,1,"true",startd,endd);//群聊天发送消息
		List<Tongji> qunMsgReceivedSize = wxMessageService.queryTongji(cid,1,"false",startd,endd);//群聊天收到消息
		
		Map<String,Object> data= new HashMap<>();
		data.put("chatMsgSendSize",chatMsgSendSize);
		data.put("chatMsgReceivedSize",chatMsgReceivedSize);
		data.put("qunMsgSendSize",qunMsgSendSize);
		data.put("qunMsgReceivedSize",qunMsgReceivedSize);
		  
		return ResultInfo.success(data);
	}
	
	@PostMapping("/circleTongji")
	@ResponseBody
	public ResultInfo circleTongji(Integer cid) {
		//发朋友圈数
		Integer  circleSize = circleTaskService.queryCircleSize(cid);
		//被点赞数
		Integer  circleLikeSize = circleTaskService.queryCircleLikeSize(cid);
		//被评论数
		Integer  circleCommentSize = circleTaskService.queryCircleCommentSize(cid);
		//好友总数
		Integer  friendSize = wxContactService.findContactinfoByCidType(cid, 0);
		//好友去重总数
		Integer  friendDistSize = wxContactService.findContactinfoByCidTypeDistWechatId(cid, 0);
		
		Map<String,Object> data= new HashMap<>();
		data.put("circleSize",circleSize);
		data.put("circleLikeSize",circleLikeSize);
		data.put("circleCommentSize",circleCommentSize);
		data.put("friendSize",friendSize);
		data.put("friendDistSize",friendDistSize);
		 				  
		return ResultInfo.success(data);
	}
	
	@PostMapping("/friendAddTongji")
	@ResponseBody
	public ResultInfo friendAddTongji(FriendAddLogQuery info) {
		List<FriendCountData> data = friendAddLogService.queryFriendAddData(info);
		return ResultInfo.success(data);
	}
	
	 
	
	
	
	@PostMapping("/luckyMoneyTongji")
	@ResponseBody
	public ResultInfo luckyMoneyTongji(LuckyMoneyQuery query) {
		
		if(query.getCid() == null ||  StringUtils.isEmpty(query.getStart()) || StringUtils.isEmpty(query.getEnd())){
			return ResultInfo.fail("参数传入错误");
		}
		 
		Date startd = DateUtil.convertString2Date(query.getStart(), DateUtil.DATE_FORMAT_1);
		Date endd = DateUtil.convertString2Date(query.getEnd(), DateUtil.DATE_FORMAT_1);
		List<LuckyMoneyTongji> data = wxLuckymoneyService.queryTongji(query.getCid(), query.getType(), startd, endd);
		return ResultInfo.success(data);
	}
	
	
	@PostMapping("/luckyMoneyDetail")
	@ResponseBody
	public ResultInfo luckyMoneyDetail(LuckyMoneyQuery query) {
		
		if(query.getCid() == null ||  StringUtils.isEmpty(query.getStart()) || StringUtils.isEmpty(query.getEnd()) || StringUtils.isEmpty(query.getWechatid())){
			return ResultInfo.fail("参数传入错误");
		}
		 
		Date startd = DateUtil.convertString2Date(query.getStart(), DateUtil.DATE_FORMAT_1);
		Date endd = DateUtil.convertString2Date(query.getEnd(), DateUtil.DATE_FORMAT_1);
		List<LuckyMoneyDetail> data = wxLuckymoneyService.queryLuckyMoneyDetail(query.getCid(), query.getType(),query.getWechatid(), startd, endd);
		return ResultInfo.success(data);
	}
	
	
	@PostMapping("/friendChangeTongji")
	@ResponseBody
	public ResultInfo friendChangeTongji(FriendChangeQuery query) {
		
		if(StringUtils.isEmpty(query.getStart()) || StringUtils.isEmpty(query.getEnd())){
			return ResultInfo.fail("参数传入错误");
		}
		 
		Date startd = DateUtil.convertString2Date(query.getStart(), DateUtil.DATE_FORMAT_1);
		Date endd = DateUtil.convertString2Date(query.getEnd(), DateUtil.DATE_FORMAT_1);
		List<FriendChangeVo> data = friendChangeService.queryTongji(query.getCid(),query.getAccountid(), query.getType(), query.getWechatid(),startd, endd);
		return ResultInfo.success(data);
	}
	
	@PostMapping("/friendTotalTongji")
	@ResponseBody
	public ResultInfo friendTotalTongji(FriendChangeQuery query) {
		if(query.getCid() == null ){
			return ResultInfo.fail("参数传入错误");
		} 
		
		List<FriendVo> data=null;
		
		List<WxAccountInfo> list = wxAccountService.findWeChatAccountInfo(query.getCid(), query.getAccountid());
		if(null != list && list.size()>0) {
			List<String> weList= new ArrayList<String>();
			for(WxAccountInfo info:list) {
				weList.add(info.getWechatid());
			}
			data = wxContactService.queryTongji(query.getCid(), weList);
		}
		
		return ResultInfo.success(data); 
		
	}
 
}
