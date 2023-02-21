package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.DeviceGroup;
import com.jubotech.business.web.query.DeviceGroupQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface DeviceGroupDao extends Mapper<DeviceGroup>, MySqlMapper<DeviceGroup>{
	
	List<DeviceGroup> pageList(DeviceGroupQuery query);
	 
	List<DeviceGroup> getAllByCid(@Param("cid")Integer cid);
	
	DeviceGroup findByid(@Param("id")Integer id);
	  
	int insert(DeviceGroup info);
	
	void update(DeviceGroup info);
	
	int delete(DeviceGroup info);
  
}
