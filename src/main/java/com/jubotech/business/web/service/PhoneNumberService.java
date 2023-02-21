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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.PhoneNumberDao;
import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.domain.vo.PhoneNumberVo;
import com.jubotech.business.web.query.PhoneNumberQuery;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;
import com.jubotech.framework.domain.base.DBPage;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class PhoneNumberService {
	 
	@InitBinder
    public void initListBinder(WebDataBinder binder){
        // 设置需要包裹的元素个数，默认为256
		binder.setAutoGrowNestedPaths(true);  
        binder.setAutoGrowCollectionLimit(6000);
    }
	
	@Autowired
	private PhoneNumberDao phoneNumberDao;
 
	public PageInfo<PhoneNumberInfo> pageList(PhoneNumberQuery query) {
		  
		PageHelper.startPage(query.getPage(), query.getRows());
        
		Example example = new Example(PhoneNumberInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if(!StringUtils.isEmpty(query.getPhonenumber())){
			criteria.andLike("phonenumber", "%"+query.getPhonenumber()+"%");
		}
		if(!StringUtils.isEmpty(query.getWechatid())){
			criteria.andLike("wechatid", query.getWechatid());
		} 
		if(null != query.getState()) {
			criteria.andEqualTo("state", query.getState());
		}
		example.orderBy("id").desc();
		return new PageInfo<>(phoneNumberDao.selectByExample(example));
		
	}

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(PhoneNumberInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return phoneNumberDao.deleteByExample(example);
	}
	
	public PhoneNumberInfo getByid(Integer id){
		PhoneNumberInfo user = phoneNumberDao.selectByPrimaryKey(id);
		return user;
	}
	
	public ResultInfo insert(PhoneNumberInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			phoneNumberDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	public ResultInfo update(PhoneNumberInfo info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			phoneNumberDao.update(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}

	public void delete(Integer id) {
		PhoneNumberInfo info = new PhoneNumberInfo();
		info.setId(id);
		phoneNumberDao.delete(info);
	}
	
	
	
	public Integer queryNotUsePhoneNumberCount(Integer cid){
		return phoneNumberDao.queryNotUsePhoneNumberCount(cid);
	}
 
	public List<PhoneNumberInfo> queryPhoneNumberInfos(PhoneNumberVo info){
		DBPage page = new DBPage(info.getPageSize(), (info.getPageNo() - 1) * info.getPageSize());
		return phoneNumberDao.queryPhoneNumberInfos(page,info);
	}
	
	public Integer queryPhoneNumberInfosCount(PhoneNumberVo info){
		DBPage page = new DBPage(info.getPageSize(), (info.getPageNo() - 1) * info.getPageSize());
		return phoneNumberDao.queryPhoneNumberInfosCount(page,info);
	}
	
  
}
