package com.jubotech.business.web.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.FriendChangeDao;
import com.jubotech.business.web.domain.FriendChange;
import com.jubotech.business.web.domain.vo.FriendChangeVo;
import com.jubotech.framework.common.ResultInfo;

@Service
@Transactional // 支持事务
public class FriendChangeService {

	@Resource
	private FriendChangeDao friendChangeDao;
 

	public ResultInfo insert(FriendChange info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			friendChangeDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public List<FriendChangeVo> queryTongji(Integer cid,Integer accountid,Integer type,String wechatid,Date start,Date end){
		return friendChangeDao.queryTongji(cid,accountid,type,wechatid,start,end);
	}
}
