package com.jubotech.business.web.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.service.CircleLikeService;
import com.jubotech.framework.common.ResultInfo;

@Controller
@RequestMapping("/user/circleLike")
public class CircleLikeController {

	@Autowired
	private CircleLikeService service;

	@PostMapping("/queryLikesById")
	@ResponseBody
	public ResultInfo queryLikesById(Integer id) {
		return ResultInfo.success(service.getByCircleId(id));
	}

}
