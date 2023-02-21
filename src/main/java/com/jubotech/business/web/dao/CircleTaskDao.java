package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CircleTask;
import com.jubotech.business.web.query.CircleTaskQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CircleTaskDao extends Mapper<CircleTask>, MySqlMapper<CircleTask> {
	List<CircleTask> findTaskTimeByTime(@Param("execute_time") String execute_time);
	List<CircleTask> pageList(CircleTaskQuery query);
	List<CircleTask> findLikeCommentsize(@Param("cid") Integer cid );
}
