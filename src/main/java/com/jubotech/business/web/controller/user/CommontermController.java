package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CommontermInfo;
import com.jubotech.business.web.query.CommonTermQuery;
import com.jubotech.business.web.service.CommontermService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/commonterm")
public class CommontermController {
	 
	@Autowired
	private CommontermService service;

	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CommonTermQuery query) {
		GridPage<CommontermInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(CommontermInfo info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(CommontermInfo info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}
 
}
