package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.AccountDao;
import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.query.AccountQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.util.StringUtil;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
@CacheConfig(cacheNames="system")
public class AccountService {

	@Autowired
	private AccountDao accountDao;

	public PageInfo<AccountInfo> pageList(AccountQuery query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        return new PageInfo<>(accountDao.pageList(query));
    }
	
	@CacheEvict(/*value="system",*/allEntries=true)
	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(AccountInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);
 
		return accountDao.deleteByExample(example);
	}
	
	@Cacheable(key="#accountIds")
	public List<AccountInfo> queryByIds(String accountIds) {
		Example example = new Example(AccountInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", StringUtil.stringToList(accountIds));
		return accountDao.selectByExample(example);
    }

	public AccountInfo findAccountInfoByid(Integer id) {
		AccountInfo user = accountDao.findAccountInfoByid(id);
		return user;
	}
	
	@Cacheable(/*value="system",*/key="#wechatId")
	public AccountInfo findAccountInfoByWeChatId(String  wechatId) {
		AccountInfo user = accountDao.findAccountInfoByWeChatId(wechatId);
		return user;
	}
	 
	public AccountInfo findAccountInfoByAccount(String account) {
		AccountInfo user = accountDao.findAccountInfoByAccount(account);
		return user;
	}
	 
	public List<AccountInfo> getAllAccountInfoByCid(Integer cid){
		return accountDao.getAllAccountInfoByCid(cid);
	}

	@CacheEvict(/*value="system",*/allEntries=true)
	public ResultInfo insert(AccountInfo user) {
		ResultInfo res = new ResultInfo();
		try {
			user.setCreateTime(new Date());
			accountDao.insert(user);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败，客服账号已被占用请换一个");
		}
		return res;
	}
	
	@CacheEvict(/*value="system",*/allEntries=true)
	public ResultInfo update(AccountInfo user) {
		ResultInfo res = new ResultInfo();
		try {
			user.setCreateTime(new Date());
			accountDao.updateByPrimaryKey(user);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	@CacheEvict(/*value="system",*/allEntries=true)
	public void delete(Integer id) {
		AccountInfo user = new AccountInfo();
		user.setId(id);
		accountDao.delete(user);
	}
	
	public AccountInfo login(String pname,String password) {
		AccountInfo user = accountDao.findAccountInfoByAccountPwd(pname, password);
		return user;
	}
	
	 
	
	public ResultInfo pwdedit(String newpwd,String oldpwd,Integer userid){
		ResultInfo res = ResultInfo.success();
		AccountInfo  account = findAccountInfoByid(userid);
		if(null == account){
			return ResultInfo.fail("参数传入错误");
		}
		if(!account.getPassword().equals(oldpwd)){
			return ResultInfo.fail("原密码错误");
		}
		try {
			accountDao.pwdedit(newpwd,userid);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultInfo.fail("密码修改失败");
		}
		return res;
	}
	
	
	public AccountInfo clientlogin(String loginname,String password) {
		return accountDao.findAccountInfo(loginname, password,1);
	}
	

}
