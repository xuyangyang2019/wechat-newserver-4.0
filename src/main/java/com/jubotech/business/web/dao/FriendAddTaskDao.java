package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.FriendAddTask;
import com.jubotech.business.web.query.FriendAddTaskQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FriendAddTaskDao  extends Mapper<FriendAddTask>, MySqlMapper<FriendAddTask>{
	
	List<FriendAddTask> pageList(FriendAddTaskQuery query);
	 
	FriendAddTask findFriendAddTaskByid(@Param("id")Integer id);
	 
	int insert(FriendAddTask info);
	
	void update(FriendAddTask info);
	
	int delete(FriendAddTask info);
	
	List<FriendAddTask> findFriendAddTaskByTime(@Param("execute_time") String execute_time);
}
