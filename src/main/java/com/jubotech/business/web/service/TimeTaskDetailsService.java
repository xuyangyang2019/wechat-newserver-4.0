package com.jubotech.business.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.TimeTaskDetailsDao;
import com.jubotech.business.web.domain.TimeTaskDetails;

@Service
@Transactional // 支持事务
public class TimeTaskDetailsService {

	@Autowired
	private TimeTaskDetailsDao timeTaskDetailsDao;
	
	public void deleteByTid(Integer tid) {
		timeTaskDetailsDao.deleteByTid(tid);
	}
	
	
	public void insert(TimeTaskDetails info) {
		timeTaskDetailsDao.insert(info);
	}

	public TimeTaskDetails findTimeTaskDetailsByid(Long id) {
		return timeTaskDetailsDao.findTimeTaskDetailsByid(id);
	}
	
	public TimeTaskDetails findTimeTaskDetailsByMsgId(String msgId) {
		return timeTaskDetailsDao.findTimeTaskDetailsByMsgId(msgId);
	}
	
	public List<TimeTaskDetails> findTimeTaskDetailsByTid(Integer tid) {
		return timeTaskDetailsDao.findTimeTaskDetailsByTid(tid,1);
	}

	public void updateResults(TimeTaskDetails info) {
		timeTaskDetailsDao.updateResults(info);
	}
	
	public void updateState(TimeTaskDetails info) {
		timeTaskDetailsDao.updateState(info);
	}
	
	public void delete(TimeTaskDetails info) {
		timeTaskDetailsDao.deleteByPrimaryKey(info);
	}

}
