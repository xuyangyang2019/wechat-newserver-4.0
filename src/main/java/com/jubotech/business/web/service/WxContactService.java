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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jubotech.business.web.dao.WxContactDao;
import com.jubotech.business.web.domain.WxContactInfo;
import com.jubotech.business.web.domain.vo.FriendVo;
import com.jubotech.business.web.query.WeChatContactQuery;
import com.jubotech.framework.common.ServiceException;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class WxContactService{

	@Autowired
	private WxContactDao weChatContactDao;
	
	
	public List<FriendVo> queryTongji(Integer cid,List<String> wechatids){
		return weChatContactDao.queryTongji(cid, wechatids);
	}

	public PageInfo<WxContactInfo> pageList(WeChatContactQuery query) {
		PageHelper.startPage(query.getPage(), query.getRows());
         
		Example example = new Example(WxContactInfo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("cid", query.getCid());
		if(!StringUtils.isEmpty(query.getFriend_wechatno())){
			criteria.andEqualTo("friend_wechatno", query.getFriend_wechatno());
			criteria.orEqualTo("friendid", query.getFriend_wechatno());
		}
		if(!StringUtils.isEmpty(query.getWechatid())){
			criteria.andLike("wechatid", "%"+query.getWechatid()+"%");
		}
		if(!StringUtils.isEmpty(query.getNickname())){
			criteria.andLike("nickname", "%"+query.getNickname()+"%");
		}
		if(null != query.getType()) {
			criteria.andEqualTo("type", query.getType());
		}
		  
		example.orderBy("id").desc();
		return new PageInfo<>(weChatContactDao.selectByExample(example));
		 
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
	
	
	public Integer queryFriendAddCount(String wechatid,Date start,Date end) {
		return weChatContactDao.queryFriendAddCount(wechatid, start, end);
	}
	
	public WxContactInfo findContactinfoByid(Integer id) {
		return weChatContactDao.findContactinfoByid(id);
	}

	public WxContactInfo findContactinfoByfriendid(Integer cid, String wechatid, String friendid) {
		return weChatContactDao.findContactinfoByfriendid(cid, wechatid, friendid);
	}

	public List<WxContactInfo> findContactinfoByWeChatId(Integer cid, String wechatid) {
		return weChatContactDao.findContactinfoByWeChatId(cid, wechatid);
	}
	
	public WxContactInfo findContactinfoByWechatidFriendid(String wechatid, String friendid) {
		return weChatContactDao.findContactinfoByWechatidFriendid(wechatid, friendid);
	}
	
	public  Integer findContactinfoByCidType(Integer cid,Integer type){
		return weChatContactDao.findContactinfoByCidType(cid, type);
	}
	
	public  Integer findContactinfoByCidTypeDistWechatId(Integer cid,Integer type){
		return weChatContactDao.findContactinfoByCidTypeDistWechatId(cid, type);
	}
	 
	public void insert(WxContactInfo info) {
		weChatContactDao.insert(info);
	}

	public void update(WxContactInfo info) {
		weChatContactDao.update(info);
	}

	public void delete(Integer id) {
		WxContactInfo info = new WxContactInfo();
		info.setId(id);
		weChatContactDao.delete(info);
	}

}
