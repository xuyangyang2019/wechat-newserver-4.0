package com.jubotech.business.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.FriendAddLog;
import com.jubotech.business.web.domain.FriendCountData;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FriendAddLogDao extends Mapper<FriendAddLog>, MySqlMapper<FriendAddLog>{
	
	List<FriendCountData> queryFriendAddDataByDay(@Param("cid") Integer cid,@Param("wechatid") String wechatid,@Param("groupid") Integer groupid,@Param("start")  Date start, @Param("end") Date end);
	 
	List<FriendCountData> queryFriendAddDataByHour(@Param("cid") Integer cid,@Param("wechatid") String wechatid,@Param("groupid") Integer groupid,@Param("start")  Date start, @Param("end") Date end);
}
