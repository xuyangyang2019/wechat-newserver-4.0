package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CircleTask;
import com.jubotech.business.web.query.CircleTaskQuery;
import com.jubotech.business.web.service.CircleTaskService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/circle")
public class CircleController{

	@Autowired
	private CircleTaskService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CircleTaskQuery query) {
		GridPage<CircleTask> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(CircleTask info) {
		return service.insert(info);
	}
  
	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		return service.deleteByIds(ids);
	}
	
	
	@PostMapping("/queryWechats")
	@ResponseBody
	public ResultInfo findWxAccountByTaskId(Integer id) {
		return service.findWxAccountByTaskId(id);
	}
	
	
	@PostMapping("/queryCircleTaskInfo")
	@ResponseBody
	public ResultInfo queryCircleTaskInfo(Integer id) {
		return ResultInfo.success(service.findByTid(id));
	}
	
	@PostMapping("/resendCircle")
	@ResponseBody
	public ResultInfo resendCircle(Integer id) {
		return service.resendCircle(id);
	}

}
