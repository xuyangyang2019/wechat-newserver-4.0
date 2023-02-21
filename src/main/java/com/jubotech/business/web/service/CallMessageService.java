package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.CallMessageDao;
import com.jubotech.business.web.domain.CallMessage;
import com.jubotech.business.web.query.CallMessageQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CallMessageService {

	@Resource
	private CallMessageDao callMessageDao;

	public PageInfo<CallMessage> pageList(CallMessageQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
          
		Example example = new Example(CallMessage.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if (StringUtils.isNotBlank(query.getNumber())) {
			criteria.andLike("number", "%" + query.getNumber() + "%");
		}
		if (StringUtils.isNotBlank(query.getContent())) {
			criteria.andLike("content", "%" + query.getContent() + "%");
		}
		if (StringUtils.isNotBlank(query.getRecord())) {
			criteria.andLike("record", "%" + query.getRecord() + "%");
		}
		if (StringUtils.isNotBlank(query.getImei())) {
			criteria.andLike("imei", "%" + query.getImei() + "%");
		}
		if(null != query.getType()) {
			criteria.andEqualTo("type", query.getType());
		}
		   
		example.orderBy("id").desc();
		return new PageInfo<>(callMessageDao.selectByExample(example));
		 
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(CallMessage.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return callMessageDao.deleteByExample(example);
	}

	 
	public CallMessage getCallMessageByid(Integer id) {
		CallMessage user = callMessageDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(CallMessage info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			callMessageDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CallMessage info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			callMessageDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

  
}
