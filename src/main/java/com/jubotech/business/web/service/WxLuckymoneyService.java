package com.jubotech.business.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jubotech.business.web.dao.WxLuckymoneyDao;
import com.jubotech.business.web.domain.WxLuckymoney;
import com.jubotech.business.web.domain.vo.LuckyMoneyDetail;
import com.jubotech.business.web.domain.vo.LuckyMoneyTongji;

@Service
@Transactional // 支持事务
public class WxLuckymoneyService {

	@Autowired
	private WxLuckymoneyDao wxLuckymoneyDao;
     
	
	public List<LuckyMoneyTongji> queryTongji(Integer cid,Integer type,Date start, Date end){
		try {
			return wxLuckymoneyDao.queryTongji(cid,type,start,end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<LuckyMoneyDetail> queryLuckyMoneyDetail(Integer cid,Integer type,String  wechatid,Date start, Date end){
		try {
			return wxLuckymoneyDao.queryLuckyMoneyDetail(cid,type,wechatid,start,end);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    
	public void insert(WxLuckymoney info) {
		try {
			wxLuckymoneyDao.insert(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Integer id) {
		WxLuckymoney info = new WxLuckymoney();
		info.setId(id);
		wxLuckymoneyDao.delete(info);
	}

}
