package com.jubotech.business.web.controller.user;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/user/device")
public class DeviceController {
	 
	@Autowired
	private WxAccountService service;
  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(HttpServletRequest request, DeviceQuery query) {
		GridPage<WxAccountInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	
	@PostMapping("/list")
	@ResponseBody
	public ResultInfo list(HttpServletRequest request, DeviceQuery query) {
		return ResultInfo.success(service.list(query));
	}
	  
	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(HttpServletRequest request, WxAccountInfo info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(HttpServletRequest request, WxAccountInfo info) {
		return service.updateDevice(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}
	
	
	@PostMapping("/group")
	@ResponseBody
	public ResultInfo group(String deviceids,Integer groupid) {
		return service.group(deviceids,groupid);
	}
	
	
	@PostMapping("/queryBygroupids")
	@ResponseBody
	public ResultInfo queryBygroupid(String groupids) {
		return service.queryBygroupid(groupids);
	}
}
