package com.jubotech.business.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.FriendChange;
import com.jubotech.business.web.domain.vo.FriendChangeVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface FriendChangeDao extends Mapper<FriendChange>, MySqlMapper<FriendChange>{
	List<FriendChangeVo> queryTongji(@Param("cid") Integer cid,@Param("accountid") Integer accountid,@Param("type") Integer function,@Param("wechatid") String wechatid,@Param("start")  Date start, @Param("end") Date end);
}
