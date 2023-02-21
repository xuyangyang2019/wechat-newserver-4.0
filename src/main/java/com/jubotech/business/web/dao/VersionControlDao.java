package com.jubotech.business.web.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.VersionControl;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface VersionControlDao extends Mapper<VersionControl>, MySqlMapper<VersionControl>{
     
	VersionControl queryVersionControlById(@Param("id") Integer id);
	
	void update(VersionControl info);
	  
	int insert(VersionControl info);

	int delete(VersionControl info);
}
