package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.WxAccountInfo;
import com.jubotech.business.web.query.DeviceQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface WxAccountDao   extends Mapper<WxAccountInfo>, MySqlMapper<WxAccountInfo>{
	 
	List<WxAccountInfo> pageList(DeviceQuery query);
	  
	Integer getPhoneAllCountByCid(@Param("cid")Integer cid);
	
	int insert(WxAccountInfo info);
	  
	void updateLoginTime(@Param("deviceid")String deviceid);
	 
	WxAccountInfo findWeChatAccountInfoByid(@Param("id")Integer id);
	
	WxAccountInfo findWeChatAccountInfoByDeviceid(@Param("deviceid")String deviceid);
	
    List<WxAccountInfo> findWeChatAccountInfoByWeChatIdNotEqualsDeviceid(@Param("wechatid")String wechatid,@Param("deviceid")String deviceid);
    
    List<WxAccountInfo> findAllAccountWechatInfo();
    
    List<WxAccountInfo> findAccountWechatInfoByCid(@Param("cid")Integer cid);
      
	Integer findAllWeChatAccount(@Param("cid")Integer cid);
	
	Integer findOnLineWeChatAccount(@Param("cid")Integer cid);
	
	WxAccountInfo findWeChatAccountInfoByWeChatId(@Param("wechatid")String wechatid);
	
	List<WxAccountInfo> findWeChatAccountInfo(@Param("cid")Integer cid ,@Param("accountid")Integer accountid);
	
	List<WxAccountInfo> findAllWeChatAccountInfo(@Param("isonline")Integer isonline);
	
	void update(WxAccountInfo info);
	
	int delete(WxAccountInfo info);
	    
}
