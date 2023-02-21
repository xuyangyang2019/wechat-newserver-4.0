package com.jubotech.business.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.CircleLikeDao;
import com.jubotech.business.web.domain.CircleLike;
import com.jubotech.business.web.domain.CircleTaskDetails;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional // 支持事务
public class CircleLikeService {

	@Autowired
	private CircleLikeDao circleLikeDao;

	@Autowired
	private CircleTaskService circleTaskService;

	public List<CircleLike> getByCircleId(Integer id) {
		List<CircleTaskDetails> lists = circleTaskService.findByTid(id);
		if (null != lists && lists.size() > 0) {
			List<String> list = new ArrayList<String>();
			for (CircleTaskDetails detail : lists) {
				list.add(detail.getMsgid());
			}

			if (null != list && list.size() > 0) {
				Example example = new Example(CircleLike.class);
				Example.Criteria criteria = example.createCriteria();
				criteria.andIn("circleid", list);
				return circleLikeDao.selectByExample(example);
			}
		}
		return null;
	}
	
	public CircleLike getCircleLikeByFriendid(String wechatid,String circleid,String friendid) {
		Example example = new Example(CircleLike.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("circle_wechatid", wechatid);
		criteria.andEqualTo("circleid", circleid);
		criteria.andEqualTo("friendid", friendid);
		return circleLikeDao.selectOneByExample(example);
	}

	public void insert(CircleLike info) {
		circleLikeDao.insert(info);
	}

}
