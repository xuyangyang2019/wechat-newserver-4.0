package com.jubotech.business.web.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.ChatGPTConversationDao;
import com.jubotech.business.web.domain.ChatGPTConversation;
import com.jubotech.framework.common.ResultInfo;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class ChatGPTConversationService {

	@Resource
	private ChatGPTConversationDao chatGPTConversationDao;

	 

	public int deleteByIds(String ids) {
		if (StringUtils.isBlank(ids)) {
			throw new ServiceException("invalid param");
		}
		String[] idArray = StringUtils.split(ids, ",");
		Set<String> userIds = Arrays.stream(idArray).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
		Example example = new Example(ChatGPTConversation.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", userIds);

		return chatGPTConversationDao.deleteByExample(example);
	}

	 
	public ChatGPTConversation getByid(Integer id) {
		ChatGPTConversation user = chatGPTConversationDao.selectByPrimaryKey(id);
		return user;
	}

	public ResultInfo insert(ChatGPTConversation info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			chatGPTConversationDao.insert(info);
		} catch (Exception e) {
			return ResultInfo.fail("添加失败");
		}
		return res;
	}

	
	public ResultInfo update(ChatGPTConversation info) {
		ResultInfo res = new ResultInfo();
		try {
			info.setCreateTime(new Date());
			chatGPTConversationDao.updateByPrimaryKey(info);
		} catch (Exception e) {
			return ResultInfo.fail("修改失败");
		}
		return res;
	}
	 

 
	public ChatGPTConversation byWechatidFriendid(String wechatid,String friendid) {
		Example example = new Example(ChatGPTConversation.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("friendid", friendid);
		criteria.andEqualTo("wechatid", wechatid);
		return chatGPTConversationDao.selectOneByExample(example);
	}

}
