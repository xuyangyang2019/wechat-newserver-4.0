package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.ConvDelLog;
import com.jubotech.business.web.query.ConvDelLogQuery;
import com.jubotech.business.web.service.ConvDelLogService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/convDelLog")
public class ConvDelLogController{

	@Autowired
	private ConvDelLogService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(ConvDelLogQuery query) {
		GridPage<ConvDelLog> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
 
}
