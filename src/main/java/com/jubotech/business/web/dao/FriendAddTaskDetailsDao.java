package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.FriendAddTaskDetails;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FriendAddTaskDetailsDao extends Mapper<FriendAddTaskDetails>, MySqlMapper<FriendAddTaskDetails>{

	FriendAddTaskDetails findFriendAddTaskDetailsByid(@Param("id") Integer id);

	int insert(FriendAddTaskDetails info);

	void deleteByTid(@Param("tid") Integer tid);
	
	void updateJsonContent(FriendAddTaskDetails info);
	 
	void updateState(FriendAddTaskDetails info);
	
	List<FriendAddTaskDetails> findFriendAddTaskDetailsByTid(@Param("tid") Integer tid,@Param("state") Integer state);
	 
	List<FriendAddTaskDetails> findFriendAddTaskDetailsByTime(@Param("execute_time") String execute_time);
	  
}
