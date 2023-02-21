package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.TimeTaskDetails;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface TimeTaskDetailsDao  extends Mapper<TimeTaskDetails>, MySqlMapper<TimeTaskDetails>{

	TimeTaskDetails findTimeTaskDetailsByid(@Param("id") Long id);
	
	TimeTaskDetails findTimeTaskDetailsByMsgId(@Param("msgid") String msgid);
	
	int insert(TimeTaskDetails info);

	void deleteByTid(@Param("tid") Integer tid);
	
	void updateJsonContent(TimeTaskDetails info);
	
	void updateResults(TimeTaskDetails info);
	
	void updateState(TimeTaskDetails info);
	
	List<TimeTaskDetails> findTimeTaskDetailsByTid(@Param("tid") Integer tid,@Param("state") Integer state);
	
	List<TimeTaskDetails> findTimeTaskDetailsByTidDate(@Param("tid") Integer tid,@Param("state") Integer state,@Param("date") String date);
	
	Integer findTimeTaskDetailsCount(@Param("tid") Integer tid,@Param("state") Integer state);
	
	Integer findTimeTaskDetailsTaskResultCount(@Param("tid") Integer tid,@Param("results") String results);
	
	int deleteTask(@Param("date") String date);
	 
}
