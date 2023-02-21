package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CircleTaskDetails;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CircleTaskDetailsDao extends Mapper<CircleTaskDetails>, MySqlMapper<CircleTaskDetails> {
	List<CircleTaskDetails> findTimeTaskDetailsByTid(@Param("tid") Integer tid,@Param("state") Integer state);
	List<CircleTaskDetails> findTimeTaskDetailsByTidDate(@Param("tid") Integer tid,@Param("state") Integer state,@Param("date") String date);
}
