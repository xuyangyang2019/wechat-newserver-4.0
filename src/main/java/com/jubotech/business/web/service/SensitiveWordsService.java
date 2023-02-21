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
import com.jubotech.business.web.dao.SensitiveWordsDao;
import com.jubotech.business.web.domain.SensitiveWords;
import com.jubotech.business.web.query.SensitiveWordsQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class SensitiveWordsService {

	@Resource
	private SensitiveWordsDao sensitiveWordsDao;

	public PageInfo<SensitiveWords> pageList(SensitiveWordsQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(SensitiveWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if (StringUtils.isNotBlank(query.getWords())) {
			criteria.andLike("words", "%" + query.getWords() + "%");
		}
		 
		example.orderBy("id").desc();
		return new PageInfo<>(sensitiveWordsDao.selectByExample(example));
		 
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(SensitiveWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return sensitiveWordsDao.deleteByExample(example);
	}

	 
	public SensitiveWords getByid(Integer id) {
		SensitiveWords user = sensitiveWordsDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(SensitiveWords info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			sensitiveWordsDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(SensitiveWords info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			sensitiveWordsDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

 
	public List<SensitiveWords> listByCid(Integer cid) {
		Example example = new Example(SensitiveWords.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", cid);
		return sensitiveWordsDao.selectByExample(example);
	}

}
