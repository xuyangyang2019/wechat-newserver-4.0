package com.jubotech.business.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.AccountInfo;
import com.jubotech.business.web.query.AccountQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface AccountDao extends Mapper<AccountInfo>, MySqlMapper<AccountInfo>{
	
	List<AccountInfo> pageList(AccountQuery query);
  
	AccountInfo findAccountInfoByid(@Param("id")Integer id); 
	
	AccountInfo findAccountInfoByWeChatId(@Param("wechatId")String wechatId); 
	
	AccountInfo findAccountInfoByAccount(@Param("account")String account);
	  
	AccountInfo findAccountInfo(@Param("account")String account,@Param("password")String password,@Param("type")Integer type);
	
	AccountInfo findAccountInfoByAccountPwd(@Param("account")String account,@Param("password")String password);
	
	void pwdedit(@Param("password")String password,@Param("id")Integer id);
	  
	List<AccountInfo> getAllAccountInfoByCid(@Param("cid")Integer cid);
	  
}
