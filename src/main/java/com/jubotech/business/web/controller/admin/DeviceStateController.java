package com.jubotech.business.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.query.DeviceQuery;
import com.jubotech.business.web.service.WxAccountService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/admin/device")
public class DeviceStateController {
	
	@Autowired
	private WxAccountService service;
    
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(DeviceQuery query) {
		GridPage<WxAccountInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
     
}
