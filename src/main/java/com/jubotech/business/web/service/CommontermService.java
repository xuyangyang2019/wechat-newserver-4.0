package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.CommontermDao;
import com.jubotech.business.web.domain.CommontermInfo;
import com.jubotech.business.web.query.CommonTermQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CommontermService {

	@Autowired
	private CommontermDao commontermDao;

	public PageInfo<CommontermInfo> pageList(CommonTermQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(commontermDao.pageList(query));
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(CommontermInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return commontermDao.deleteByExample(example);
	}


 
	public List<CommontermInfo> getAllCommontermInfoByCid(Integer cid,String name){
		List<CommontermInfo> list = commontermDao.getAllCommontermInfoByCid(cid,name);
		return list;
	}
	 
	public CommontermInfo getCommontermInfoByid(Integer id){
		CommontermInfo user = commontermDao.findCommontermInfoByid(id);
		return user;
	}
	
	public ResultInfo insert(CommontermInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontermDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CommontermInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontermDao.update(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		CommontermInfo info = new CommontermInfo();
		info.setId(id);
		commontermDao.delete(info);
	}
	
}
