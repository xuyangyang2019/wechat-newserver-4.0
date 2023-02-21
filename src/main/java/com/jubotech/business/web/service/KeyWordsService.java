package com.jubotech.business.web.service;

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
import com.jubotech.business.web.dao.KeyWordsDao;
import com.jubotech.business.web.domain.KeyWords;
import com.jubotech.business.web.query.KeyWordsQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class KeyWordsService {

	@Resource
	private KeyWordsDao keyWordsDao;

	public PageInfo<KeyWords> pageList(KeyWordsQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(KeyWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		criteria.andEqualTo("wechatid", query.getWechatid());
		if (StringUtils.isNotBlank(query.getKeyWord())) {
			criteria.andLike("keyWord", "%" + query.getKeyWord() + "%");
		}
		if(null != query.getKeyType()) {
			criteria.andEqualTo("keyType", query.getKeyType());
		}
		if(null != query.getResourceType()) {
			criteria.andEqualTo("resourceType", query.getResourceType());
		}
		example.orderBy("id").desc();
		return new PageInfo<>(keyWordsDao.selectByExample(example));
		 
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(KeyWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return keyWordsDao.deleteByExample(example);
	}

	 
	public KeyWords getKeyWordsByid(Integer id) {
		KeyWords user = keyWordsDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(KeyWords info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			keyWordsDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(KeyWords info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			keyWordsDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

 
	public List<KeyWords> listByAccountIdKeyType(String wechatid,Integer keyType) {
		Example example = new Example(KeyWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("keyType", keyType);
		criteria.andEqualTo("wechatid", wechatid);
		return keyWordsDao.selectByExample(example);
	}

}
