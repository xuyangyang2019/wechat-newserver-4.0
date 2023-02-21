package com.jubotech.business.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.Tongji;
import com.jubotech.business.web.domain.WxMessageInfo;
import com.jubotech.business.web.query.MessageQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface WxMessageDao  extends Mapper<WxMessageInfo>, MySqlMapper<WxMessageInfo>{
	
	List<WxMessageInfo> pageList(MessageQuery query);
     
	WxMessageInfo queryWeChatMessageInfoByMsgServerId(@Param("wechatid") String wechatid ,@Param("friendid") String friendid,@Param("msgSvrId") String msgSvrId);
	
	Integer queryWeChatMessageCountByCidType(@Param("cid") Integer cid,@Param("type") Integer type);
	
	List<Tongji> queryTongji(@Param("cid") Integer cid,@Param("type") Integer type,@Param("isSend")  String isSend,@Param("start")  Date start, @Param("end") Date end);
	
	void update(WxMessageInfo info);
	
	void updateContent(WxMessageInfo info);
	
	int insert(WxMessageInfo info);

	int delete(WxMessageInfo info);
}
