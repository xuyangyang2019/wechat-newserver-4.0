package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CircleInfo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CircleDao  extends Mapper<CircleInfo>, MySqlMapper<CircleInfo>{

	int insert(CircleInfo info);
	 
	void update(CircleInfo info);
	
	CircleInfo findCircleInfoByWeChatIdCircleId(@Param("wechatid")String wechatid ,@Param("circleid")String circleid);
	
	List<CircleInfo> queryCircleInfoByInserttime(@Param("dat")String dat);
	 
}
