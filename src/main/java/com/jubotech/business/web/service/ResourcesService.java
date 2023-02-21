package com.jubotech.business.web.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.ResourcesDao;
import com.jubotech.business.web.domain.Resources;
import com.jubotech.business.web.query.ResourcesQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class ResourcesService {

	@Resource
	private ResourcesDao resourcesDao;

	public PageInfo<Resources> pageList(ResourcesQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(Resources.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if(null != query.getType()){
			if(query.getType()==1){
				List<Integer> list= new ArrayList<Integer>();
				list.add(1);
				list.add(3);
				list.add(4);
				criteria.andIn("type", list);
			}else{
				criteria.andEqualTo("type", query.getType());
			}
		}
		
		if(!StringUtils.isEmpty(query.getContent())) {
			criteria.andLike("content", "%" + query.getContent() + "%");
		}
		
		example.orderBy("id").desc();
		return new PageInfo<>(resourcesDao.selectByExample(example));
		 
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(Resources.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return resourcesDao.deleteByExample(example);
	}

	 
	public Resources getByid(Integer id) {
		Resources user = resourcesDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(Resources info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			resourcesDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(Resources info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			resourcesDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

 
	 

}
