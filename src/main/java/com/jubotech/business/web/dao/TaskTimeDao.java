package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.TaskTimeInfo;
import com.jubotech.business.web.query.TaskQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface TaskTimeDao extends Mapper<TaskTimeInfo>, MySqlMapper<TaskTimeInfo>{
	  
	TaskTimeInfo findTaskTimeInfoByid(@Param("id")Integer id);
	 
	int insert(TaskTimeInfo info);
	
	void update(TaskTimeInfo info);
	
	int delete(TaskTimeInfo info);
	 
	void updateState(TaskTimeInfo info);
	
	int deleteTask(@Param("date") String date);
	
	List<TaskTimeInfo> findTaskTimeByTime(@Param("execute_time") String execute_time);
	
	List<TaskTimeInfo> pageList(TaskQuery query);
}
