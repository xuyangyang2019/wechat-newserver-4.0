package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.AccountDao;
import com.jubotech.business.web.dao.CustomerDao;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.domain.CustomerInfo;
import com.jubotech.business.web.query.CustomerQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CustomerService {

	@Autowired
	private CustomerDao customerDao;
	 
	@Autowired
	private AccountDao accountDao;


	public PageInfo<CustomerInfo> pageList(CustomerQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(CustomerInfo.class);
		Example.Criteria criteria = example.createCriteria();

		if (StringUtils.isNotBlank(query.getSuppliername())) {
			criteria.andLike("suppliername", "%" + query.getSuppliername() + "%");
		}
		if (StringUtils.isNotBlank(query.getAdmin())) {
			criteria.andLike("admin", "%" + query.getAdmin() + "%");
		}

		example.orderBy("id").desc();

		return new PageInfo<>(customerDao.selectByExample(example));
		 
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		for(int i=0;i<idArray.length;i++) {
			CustomerInfo user = customerDao.findCustomerInfoByid(Integer.valueOf(idArray[i]));
			Example example = new Example(AccountInfo.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("account", user.getAdmin());
			criteria.andEqualTo("type", 0);
			accountDao.deleteByExample(example);
		}
		 
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(CustomerInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return customerDao.deleteByExample(example);
	}

 
	public CustomerInfo getCustomerInfoByid(Integer id){
		CustomerInfo user = customerDao.findCustomerInfoByid(id);
		return user;
	}
	
	public ResultInfo insert(CustomerInfo user) {
		ResultInfo res = new ResultInfo();
		try {
			customerDao.insert(user);
			if (null != user.getId()) {
				AccountInfo info = new AccountInfo();
				info.setAccount(user.getAdmin());
				info.setPassword("123456");
				info.setNickname(user.getAdmin());
				info.setType(0);
				info.setCid(user.getId());
				info.setState(1);
				user.setCreateTime(new Date());
				accountDao.insert(info);
			}
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(CustomerInfo user) {
		ResultInfo res = new ResultInfo();
		try {
			user.setCreateTime(new Date());
			customerDao.update(user);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public ResultInfo delete(Integer id) {
		ResultInfo res = new ResultInfo();
		try {
			CustomerInfo user = customerDao.findCustomerInfoByid(id);
			Example example = new Example(AccountInfo.class);
			Example.Criteria criteria = example.createCriteria();
			criteria.andEqualTo("account", user.getAdmin());
			criteria.andEqualTo("type", 0);
			accountDao.deleteByExample(example);
			customerDao.delete(user);
		} catch (Exception e) {
			return ResultInfo.fail("删除失败");
		}
		return res;
	}
	
	public CustomerInfo getCustomerInfoByDeviceId(String deviceId){
		return customerDao.getCustomerInfoByDeviceId(deviceId);
	}
	
	
	
}
