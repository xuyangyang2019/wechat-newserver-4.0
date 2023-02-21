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
import com.jubotech.business.web.dao.CommontagDao;
import com.jubotech.business.web.domain.CommontagInfo;
import com.jubotech.business.web.query.CommonTagQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CommontagService {

	@Autowired
	private CommontagDao commontagDao;
	
	public PageInfo<CommontagInfo> pageList(CommonTagQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
		return new PageInfo<>(commontagDao.pageList(query));
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(CommontagInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return commontagDao.deleteByExample(example);
	}


 
	public List<CommontagInfo> getAllCommontagInfoByCid(Integer cid){
		List<CommontagInfo> list = commontagDao.getAllCommontagInfoByCid(cid);
		return list;
	}
	 
	public CommontagInfo getCommontagInfoByid(Integer id){
		CommontagInfo user = commontagDao.findCommontagInfoByid(id);
		return user;
	}
	
	public ResultInfo insert(CommontagInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontagDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CommontagInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			commontagDao.update(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		CommontagInfo info = new CommontagInfo();
		info.setId(id);
		commontagDao.delete(info);
	}
	 
	
}
