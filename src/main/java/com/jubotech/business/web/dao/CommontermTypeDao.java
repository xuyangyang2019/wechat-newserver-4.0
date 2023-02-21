package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CommontermType;
import com.jubotech.business.web.query.CommonTermTypeQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CommontermTypeDao extends Mapper<CommontermType>, MySqlMapper<CommontermType>{
	
	List<CommontermType> pageList(CommonTermTypeQuery query);
	 
	List<CommontermType> getAllCommontermTypeByCid(@Param("cid")Integer cid);
	
	CommontermType findCommontermTypeByid(@Param("id")Integer id);
	  
	int insert(CommontermType info);
	
	void update(CommontermType info);
	
	int delete(CommontermType info);
  
}
