package com.jubotech.business.web.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CommontermType;
import com.jubotech.business.web.query.CommonTermTypeQuery;
import com.jubotech.business.web.service.CommontermTypeService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/commontermtype")
public class CommontermTypeController {
	
	@Autowired
	private CommontermTypeService service;
	   
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CommonTermTypeQuery query) {
		GridPage<CommontermType> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	 
	
	@PostMapping("/queryByCid")
	@ResponseBody
	public ResultInfo queryByCid(@RequestParam(value = "cid") Integer cid) {
		ResultInfo res = ResultInfo.success();
		List<CommontermType> list =  service.getAllCommontermTypeByCid(cid);
		res.setData(list);
		return res;
	}

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(CommontermType info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(CommontermType info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		return service.deleteByIds(ids);
	}
    
}
