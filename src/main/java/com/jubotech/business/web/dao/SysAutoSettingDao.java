package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.SysAutoSetting;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface SysAutoSettingDao extends Mapper<SysAutoSetting>, MySqlMapper<SysAutoSetting>{

	SysAutoSetting findSysAutoSettingByWeChatId(@Param("cid")Integer cid,@Param("auto_type")Integer auto_type,@Param("wechatId")String wechatId); 
 
	SysAutoSetting findSettingByWcIdAutoType(@Param("wechatId")String wechatId,@Param("auto_type")Integer auto_type);
	
	int insert(SysAutoSetting info);
	   
	int delete(SysAutoSetting info);
	 
	List<SysAutoSetting> getAllSysAutoSetting(@Param("cid")Integer cid,@Param("wechatId")String wechatId);
    
}
