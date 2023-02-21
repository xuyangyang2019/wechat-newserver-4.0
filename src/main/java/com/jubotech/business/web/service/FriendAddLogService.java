package com.jubotech.business.web.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.FriendAddLogDao;
import com.jubotech.business.web.domain.FriendAddLog;
import com.jubotech.business.web.domain.FriendCountData;
import com.jubotech.business.web.query.FriendAddLogQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.util.DateUtil;

@Service
@Transactional // 支持事务
public class FriendAddLogService {

	@Resource
	private FriendAddLogDao friendAddLogDao;
 
	public ResultInfo insert(FriendAddLog info) {
		ResultInfo res = new ResultInfo();
		try {
			friendAddLogDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	
	public List<FriendCountData> queryFriendAddData(FriendAddLogQuery info) {
		try {
			if(null != info && null != info.getCid() && null != info.getType() && !StringUtils.isEmpty(info.getStart()) && !StringUtils.isEmpty(info.getEnd())) {
				List<FriendCountData>  data=null;
				if(info.getType()==1) {//1按天
					Date start = DateUtil.convertString2Date(info.getStart(),DateUtil.DATE_FORMAT_1);
					Date end = DateUtil.convertString2Date(info.getEnd(),DateUtil.DATE_FORMAT_1);
					data = friendAddLogDao.queryFriendAddDataByDay(info.getCid(), info.getWechatid(), info.getGroupid(), start, end);
				}else {
					Date start = DateUtil.convertString2Date(info.getStart(),DateUtil.DATE_FORMAT_2);
					Date end = DateUtil.convertString2Date(info.getEnd(),DateUtil.DATE_FORMAT_2);
					data = friendAddLogDao.queryFriendAddDataByHour(info.getCid(), info.getWechatid(), info.getGroupid(), start, end);
				}
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 
 
	 

}
