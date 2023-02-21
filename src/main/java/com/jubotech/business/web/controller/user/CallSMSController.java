package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CallMessage;
import com.jubotech.business.web.domain.SMSMessage;
import com.jubotech.business.web.query.CallMessageQuery;
import com.jubotech.business.web.query.SMSMessageQuery;
import com.jubotech.business.web.service.CallMessageService;
import com.jubotech.business.web.service.SMSMessageService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user")
public class CallSMSController{

	@Autowired
	private CallMessageService callMessageService;
	
	
	@Autowired
	private SMSMessageService smsMessageService;
	
	
	@PostMapping("/call/pageList")
	@ResponseBody
	public ResultInfo pageList(CallMessageQuery query) {
		GridPage<CallMessage> gridPage =  new GridPage<>(callMessageService.pageList(query));
		return ResultInfo.success(gridPage);
	}
 
	@PostMapping("/sms/pageList")
	@ResponseBody
	public ResultInfo smspageList(SMSMessageQuery query) {
		GridPage<SMSMessage> gridPage = new GridPage<>(smsMessageService.pageList(query));
		return ResultInfo.success(gridPage);
	}
 

}
