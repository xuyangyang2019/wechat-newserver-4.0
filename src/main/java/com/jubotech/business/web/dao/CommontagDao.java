package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CommontagInfo;
import com.jubotech.business.web.query.CommonTagQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CommontagDao extends Mapper<CommontagInfo>, MySqlMapper<CommontagInfo>{
	List<CommontagInfo> pageList(CommonTagQuery query);
	   
	List<CommontagInfo> getAllCommontagInfoByCid(@Param("cid")Integer cid);
	  
	CommontagInfo findCommontagInfoByid(@Param("id")Integer id);
	  
	int insert(CommontagInfo info);
	
	void update(CommontagInfo info);
	
	int delete(CommontagInfo info);
  
}
