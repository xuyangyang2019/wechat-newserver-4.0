package com.jubotech.business.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.domain.vo.FriendVo;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface WxContactDao extends Mapper<WxContactInfo>, MySqlMapper<WxContactInfo>{
	
	WxContactInfo findContactinfoByid(@Param("id") Integer id);
	
	WxContactInfo findContactinfoByfriendid(@Param("cid") Integer cid,@Param("wechatid") String wechatid ,@Param("friendid") String friendid);

	WxContactInfo findContactinfoByWechatidFriendid(@Param("wechatid") String wechatid ,@Param("friendid") String friendid);
	
	List<WxContactInfo> findContactinfoByWeChatId(@Param("cid") Integer cid,@Param("wechatid") String wechatid);
	
	Integer findContactinfoByCidType(@Param("cid") Integer cid,@Param("type") Integer type);
	
	Integer findContactinfoByCidTypeDistWechatId(@Param("cid") Integer cid,@Param("type") Integer type);
	
	Integer queryFriendAddCount(@Param("wechatid") String wechatid,@Param("start")  Date start, @Param("end") Date end);
	
	List<FriendVo> queryTongji(@Param("cid") Integer cid,@Param("list")List<String> list);
	
	int insert(WxContactInfo info);

	void update(WxContactInfo info);

	int delete(WxContactInfo info);
}
