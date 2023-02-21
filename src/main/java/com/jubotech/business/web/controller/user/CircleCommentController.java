package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.CircleComment;
import com.jubotech.business.web.domain.vo.CircleCommentVo;
import com.jubotech.business.web.query.CircleCommentQuery;
import com.jubotech.business.web.service.CircleCommentService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/circleComment")
public class CircleCommentController{

	@Autowired
	private CircleCommentService service;
	  
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(CircleCommentQuery query) {
		GridPage<CircleComment> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}
	
	@PostMapping("/pageList1")
	@ResponseBody
	public ResultInfo pageList1(CircleCommentQuery query) {
		GridPage<CircleComment> gridPage = new GridPage<>(service.pageList1(query));
		return ResultInfo.success(gridPage);
	}
 
	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		return service.deleteByIds(ids);
	}
	
	@PostMapping("/followComment")
	@ResponseBody
	public ResultInfo followComment(CircleComment comment) {
		return service.followComment(comment);
	}
	
	@PostMapping("/followMoreComment")
	@ResponseBody
	public ResultInfo followMoreComment(CircleCommentVo comment) {
		return service.followMoreComment(comment);
	}

}
