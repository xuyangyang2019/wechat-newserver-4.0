package com.jubotech.business.web.controller.user;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.LoginLog;
import com.jubotech.business.web.service.AccountService;
import com.jubotech.business.web.service.LoginLogService;
import com.jubotech.framework.common.Constants;
import com.jubotech.framework.common.ResultInfo;

@Controller
public class UserController {

	@Autowired
	private AccountService service;
	
	@Autowired
	private LoginLogService loginLogService;
  
	/**
	 * 用户登录
	 * @param info
	 * @return
	 */
	@PostMapping("/user/login")
	@ResponseBody
	public ResultInfo login(@RequestParam(value = "account") String account,@RequestParam(value = "password") String password) {
		ResultInfo res = ResultInfo.success();
		AccountInfo user = service.login(account, password);
		if (null == user) {
			return ResultInfo.fail("账号或密码错误");
		}else{//user.getType()==-1  超级管理员
			user.setToken(Constants.TOKEN);//先写死，后期再改
			res.setData(user);
			//记录登录日志
			LoginLog log = new LoginLog();
			log.setAccount(account);
			log.setCid(user.getCid());
			log.setCreateTime(new Date());
			loginLogService.insert(log);
		}
		return res;
	}
 

	/**
	 * 密码修改
	 * @param newpwd
	 * @param oldpwd
	 * @param userid
	 * @return
	 */
	@ResponseBody
	@PostMapping("/user/pwdedit")
	public ResultInfo pwdedit(@RequestParam(value = "newpwd") String newpwd, @RequestParam(value = "oldpwd") String oldpwd,@RequestParam(value = "userid") Integer userid) {
		return service.pwdedit(newpwd, oldpwd, userid);
	}
	 
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public void web(HttpServletResponse response) throws Exception {
		response.sendRedirect("/web/index.html");
	}
}
