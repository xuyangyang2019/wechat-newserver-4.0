package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CommontagInfo;
import com.jubotech.business.web.query.CommonTagQuery;
import com.jubotech.business.web.service.CommontagService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/commontag")
public class CommontagController {
	@Autowired
	private CommontagService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CommonTagQuery query) {
		GridPage<CommontagInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	
    /**
     * 查询单条数据
     * @param id
     * @return
     */
	@PostMapping(value = "/queryById")
    @ResponseBody
    public ResultInfo queryById(@RequestParam(value = "id") Integer id){
		ResultInfo res = ResultInfo.success();
		CommontagInfo tag = service.getCommontagInfoByid(id);
    	res.setData(tag);
    	return res;
    }

	@PostMapping("/add")
	@ResponseBody
	public ResultInfo add(CommontagInfo info) {
		return service.insert(info);
	}

	@PostMapping("/update")
	@ResponseBody
	public ResultInfo update(CommontagInfo info) {
		return service.update(info);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}

}
