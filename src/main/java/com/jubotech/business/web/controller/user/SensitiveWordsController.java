package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.SensitiveWords;
import com.jubotech.business.web.query.SensitiveWordsQuery;
import com.jubotech.business.web.service.SensitiveWordsService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/sensitiveWords")
public class SensitiveWordsController{

	@Autowired
	private SensitiveWordsService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(SensitiveWordsQuery query) {
		GridPage<SensitiveWords> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(SensitiveWords info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(SensitiveWords info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}

}
