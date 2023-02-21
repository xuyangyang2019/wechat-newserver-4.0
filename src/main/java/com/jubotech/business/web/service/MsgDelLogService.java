package com.jubotech.business.web.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.MsgDelLogDao;
import com.jubotech.business.web.domain.MsgDelLog;
import com.jubotech.business.web.query.MsgDelLogQuery;
import com.jubotech.framework.common.ResultInfo;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class MsgDelLogService {

	@Resource
	private MsgDelLogDao msgDelLogDao;
 
	
	public PageInfo<MsgDelLog> pageList(MsgDelLogQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(MsgDelLog.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		
		if (StringUtils.isNotBlank(query.getWechatid())) {
			criteria.andEqualTo("wechatid", query.getWechatid());
		}
		if (StringUtils.isNotBlank(query.getFriendid())) {
			criteria.andEqualTo("friendid", query.getFriendid());
		}
		if(StringUtils.isNotBlank(query.getIssend())) {
			criteria.andEqualTo("issend", query.getIssend());
		}
		if(null != query.getContenttype()) {
			criteria.andEqualTo("contenttype", query.getContenttype());
		}
		if (StringUtils.isNotBlank(query.getContent())) {
			criteria.andLike("content", "%" + query.getContent() + "%");
		}
		
		example.orderBy("id").desc();
		return new PageInfo<>(msgDelLogDao.selectByExample(example));
		 
	}
	
	public ResultInfo insert(MsgDelLog info) {
		ResultInfo res = new ResultInfo();
		try {
			msgDelLogDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}
  
}
