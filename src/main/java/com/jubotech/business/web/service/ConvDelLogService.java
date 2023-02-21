package com.jubotech.business.web.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.ConvDelLogDao;
import com.jubotech.business.web.domain.ConvDelLog;
import com.jubotech.business.web.query.ConvDelLogQuery;
import com.jubotech.framework.common.ResultInfo;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class ConvDelLogService {

	@Resource
	private ConvDelLogDao convDelLogDao;
	
	public PageInfo<ConvDelLog> pageList(ConvDelLogQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(ConvDelLog.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		
		if (StringUtils.isNotBlank(query.getWechatid())) {
			criteria.andEqualTo("wechatid", query.getWechatid());
		}
		if (StringUtils.isNotBlank(query.getConvid())) {
			criteria.andEqualTo("convid", query.getConvid());
		}
		if (StringUtils.isNotBlank(query.getConvname())) {
			criteria.andEqualTo("convname", query.getConvname());
		}
		 
		example.orderBy("id").desc();
		return new PageInfo<>(convDelLogDao.selectByExample(example));
		 
	}
 
	public ResultInfo insert(ConvDelLog info) {
		ResultInfo res = new ResultInfo();
		try {
			convDelLogDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}
  
}
