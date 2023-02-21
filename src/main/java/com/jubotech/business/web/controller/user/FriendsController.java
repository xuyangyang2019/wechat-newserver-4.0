package com.jubotech.business.web.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.query.WeChatContactQuery;
import com.jubotech.business.web.service.WxContactService;
import com.jubotech.framework.common.GridPage;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/friends")
public class FriendsController{

	@Autowired
	private WxContactService service;
	 
	@PostMapping("/pageList")
	@ResponseBody
	public ResultInfo pageList(HttpServletRequest request, WeChatContactQuery query) {
		GridPage<WxContactInfo> gridPage = new GridPage<>(service.pageList(query));
		return ResultInfo.success(gridPage);
	}

	@PostMapping("/deletes")
	@ResponseBody
	public ResultInfo deletes(String ids) {
		service.deleteByIds(ids);
		return ResultInfo.success();
	}

}
