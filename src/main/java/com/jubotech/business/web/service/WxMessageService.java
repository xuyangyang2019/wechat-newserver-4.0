package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.binarywang.java.emoji.EmojiConverter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.WxMessageDao;
import com.jubotech.business.web.domain.Tongji;
import com.jubotech.business.web.domain.WxMessageInfo;
import com.jubotech.business.web.query.MessageDetailQuery;
import com.jubotech.business.web.query.MessageQuery;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class WxMessageService {

	@Autowired
	private WxMessageDao weChatMessageDao;
    
	public PageInfo<WxMessageInfo> pageList(MessageQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
//		Example example = new Example(WxMessageInfo.class);
//		Example.Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("cid", query.getCid());
//		if(!StringUtils.isEmpty(query.getWechatid())){
//			criteria.andLike("wechatid", "%"+query.getWechatid()+"%");
//		}
//		if(!StringUtils.isEmpty(query.getFriendid())){
//			criteria.andEqualTo("friendid", query.getFriendid());
//		}
//		if(!StringUtils.isEmpty(query.getIssend())){
//			criteria.andEqualTo("issend", query.getIssend());
//		}
//		if(null != query.getContenttype()) {
//			criteria.andEqualTo("contenttype", query.getContenttype());
//		}
//		if(null != query.getType()) {
//			criteria.andEqualTo("type", query.getType());
//		}
//		example.orderBy("id").desc();
//		return new PageInfo<>(weChatMessageDao.selectByExample(example));
		return new PageInfo<>(weChatMessageDao.pageList(query));
	}
	
	
	public WxMessageInfo QueryMessageDetail(MessageDetailQuery query) {
		Example example = new Example(WxMessageInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if(!StringUtils.isEmpty(query.getWechatid())){
			criteria.andEqualTo("wechatid", query.getWechatid());
		}
		if(!StringUtils.isEmpty(query.getFriendid())){
			criteria.andEqualTo("friendid", query.getFriendid());
		}
		if(!StringUtils.isEmpty(query.getMsgsvrid())){
			criteria.andEqualTo("msgsvrid", query.getMsgsvrid());
		}
		return weChatMessageDao.selectOneByExample(example);
	}
	
	
	public void deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		for(String id:userIds){
			delete(Integer.valueOf(id));
		}
	}
	
	public Integer queryWeChatMessageCountByCidType(Integer cid,Integer type){
		return weChatMessageDao.queryWeChatMessageCountByCidType(cid, type);
	}

	
	public List<Tongji> queryTongji(Integer cid,Integer type,String isSend,Date start, Date end){
		return weChatMessageDao.queryTongji(cid,type,isSend,start,end);
	}
  
	public WxMessageInfo queryWeChatMessageInfoByMsgServerId(String wechatid, String friendid, String msgSvrId) {
		return weChatMessageDao.queryWeChatMessageInfoByMsgServerId(wechatid, friendid, msgSvrId);
	}
	
	public void update(WxMessageInfo info) {
		try {
			info.setContent(EmojiConverter.getInstance().toAlias(info.getContent()));//将聊天内容进行转义
			weChatMessageDao.update(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateContent(WxMessageInfo info) {
		try {
			info.setContent(EmojiConverter.getInstance().toAlias(info.getContent()));//将聊天内容进行转义
			weChatMessageDao.updateContent(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public void insert(WxMessageInfo info) {
		try {
			info.setContent(EmojiConverter.getInstance().toAlias(info.getContent()));//将聊天内容进行转义
			weChatMessageDao.insert(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Integer id) {
		WxMessageInfo info = new WxMessageInfo();
		info.setId(id);
		weChatMessageDao.delete(info);
	}

}
