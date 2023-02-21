package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.MsgDelLog;
import com.jubotech.business.web.query.MsgDelLogQuery;
import com.jubotech.business.web.service.MsgDelLogService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/msgDelLog")
public class MsgDelLogController{

	@Autowired
	private MsgDelLogService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(MsgDelLogQuery query) {
		GridPage<MsgDelLog> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
 
}
