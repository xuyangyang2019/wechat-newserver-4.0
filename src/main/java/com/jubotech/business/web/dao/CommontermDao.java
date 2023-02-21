package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CommontermInfo;
import com.jubotech.business.web.query.CommonTermQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CommontermDao   extends Mapper<CommontermInfo>, MySqlMapper<CommontermInfo>{
	
	List<CommontermInfo> pageList(CommonTermQuery query);
	 
	List<CommontermInfo> getAllCommontermInfoByCid(@Param("cid")Integer cid,@Param("name")String name);
	 
	CommontermInfo findCommontermInfoByid(@Param("id")Integer id);
	  
	int insert(CommontermInfo info);
	
	void update(CommontermInfo info);
	
	int delete(CommontermInfo info);
  
}
