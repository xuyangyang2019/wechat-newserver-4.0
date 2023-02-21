package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.PhoneNumberInfo;
import com.jubotech.business.web.domain.vo.PhoneNumberVo;
import com.jubotech.framework.domain.base.DBPage;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface PhoneNumberDao extends Mapper<PhoneNumberInfo>, MySqlMapper<PhoneNumberInfo>{
	  
	List<PhoneNumberInfo> queryPhoneNumberInfos(@Param("page") DBPage page,@Param("info") PhoneNumberVo info);
	
	Integer queryPhoneNumberInfosCount(@Param("page") DBPage page,@Param("info") PhoneNumberVo info);
	
	Integer queryNotUsePhoneNumberCount(@Param("cid")Integer cid);
	
	List<PhoneNumberInfo> queryPhonesByCid(@Param("cid")Integer cid,@Param("count")Integer count);
	
	List<PhoneNumberInfo> queryPhoneNumberInfoByWechatidPhoneNumber(@Param("wechatid")String wechatid,@Param("phonenumber")String phonenumber);
	 
	int insert(PhoneNumberInfo info);
	
	void update(PhoneNumberInfo info);
	
}
