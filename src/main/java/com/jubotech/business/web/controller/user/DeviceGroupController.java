package com.jubotech.business.web.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.DeviceGroup;
import com.jubotech.business.web.query.DeviceGroupQuery;
import com.jubotech.business.web.service.DeviceGroupService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/devicegroup")
public class DeviceGroupController {
	
	@Autowired
	private DeviceGroupService service;
	   
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(DeviceGroupQuery query) {
		GridPage<DeviceGroup> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	 
	
	@PostMapping("/queryByCid")
	@ResponseBody
	public ResultInfo queryByCid(@RequestParam(value = "cid") Integer cid) {
		ResultInfo res = ResultInfo.success();
		List<DeviceGroup> list =  service.getAllByCid(cid);
		res.setData(list);
		return res;
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(DeviceGroup info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(DeviceGroup info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		return service.deleteByIds(ids);
	}
    
}
