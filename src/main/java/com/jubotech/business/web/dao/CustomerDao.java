package com.jubotech.business.web.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CustomerInfo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CustomerDao  extends Mapper<CustomerInfo>, MySqlMapper<CustomerInfo>{
	  
	CustomerInfo findCustomerInfoByid(@Param("id")Integer id);
	  
	int insert(CustomerInfo info);
	
	void update(CustomerInfo info);
	
	int delete(CustomerInfo info);
 
	Integer getCustomerPhoneCount(@Param("id")Integer id);
	
	CustomerInfo getCustomerInfoByDeviceId(@Param("deviceid")String deviceid);
}
