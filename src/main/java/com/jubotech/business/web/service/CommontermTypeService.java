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
import com.jubotech.business.web.dao.CommontermTypeDao;
import com.jubotech.business.web.domain.CommontermInfo;
import com.jubotech.business.web.domain.CommontermType;
import com.jubotech.business.web.query.CommonTermTypeQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CommontermTypeService {

	@Autowired
	private CommontermTypeDao commontermTypeDao;
	
	@Autowired
	private CommontermDao commontermDao;

	public PageInfo<CommontermType> pageList(CommonTermTypeQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(commontermTypeDao.pageList(query));
		
	}

	public ResultInfo deleteByIds(String ids) {
		ResultInfo res = new ResultInfo();
		try {
			if (StringUtils.isBlank(ids)) {
				throw new ServiceException("invalid param");
			}
			String[] idArray = StringUtils.split(ids, ",");
			Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
			
			Example example1 = new Example(CommontermInfo.class);
			Example.Criteria criteria1 = example1.createCriteria();
			criteria1.andIn("tid", userIds);
			Integer count = commontermDao.selectCountByExample(example1);
			if(count > 0){
				return ResultInfo.fail("该分类下有术语，不能删除");
			}
			 
			Example example = new Example(CommontermType.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andIn("id", userIds);
	
			commontermTypeDao.deleteByExample(example);
		} catch (Exception e) {
			return ResultInfo.fail("删除失败");
		}
		return res;	
	}

	public List<CommontermType> getAllCommontermTypeByCid(Integer cid){
		List<CommontermType> list = commontermTypeDao.getAllCommontermTypeByCid(cid);
		return list;
	}
 
	
	public CommontermType getCommontermTypeByid(Integer id){
		CommontermType user = commontermTypeDao.findCommontermTypeByid(id);
		return user;
	}
	
	public ResultInfo insert(CommontermType info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontermTypeDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CommontermType info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontermTypeDao.update(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		CommontermType info = new CommontermType();
		info.setId(id);
		commontermTypeDao.delete(info);
	}
	 
	
}
