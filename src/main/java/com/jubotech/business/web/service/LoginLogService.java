package com.jubotech.business.web.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.LoginLogDao;
import com.jubotech.business.web.domain.LoginLog;
import com.jubotech.framework.common.ResultInfo;

@Service
@Transactional // 支持事务
public class LoginLogService {

	@Resource
	private LoginLogDao loginLogDao;
 
	public ResultInfo insert(LoginLog info) {
		ResultInfo res = new ResultInfo();
		try {
			loginLogDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}
 
}
