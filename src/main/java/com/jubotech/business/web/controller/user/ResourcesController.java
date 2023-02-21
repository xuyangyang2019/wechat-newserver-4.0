package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.Resources;
import com.jubotech.business.web.query.ResourcesQuery;
import com.jubotech.business.web.service.ResourcesService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/resources")
public class ResourcesController{

	@Autowired
	private ResourcesService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(ResourcesQuery query) {
		GridPage<Resources> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(Resources info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(Resources info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}

}
