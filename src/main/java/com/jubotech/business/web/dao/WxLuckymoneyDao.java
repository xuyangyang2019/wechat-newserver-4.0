package com.jubotech.business.web.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.WxLuckymoney;
import com.jubotech.business.web.domain.vo.LuckyMoneyDetail;
import com.jubotech.business.web.domain.vo.LuckyMoneyTongji;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface WxLuckymoneyDao  extends Mapper<WxLuckymoney>, MySqlMapper<WxLuckymoney>{
      List<LuckyMoneyTongji> queryTongji(@Param("cid") Integer cid,@Param("type") Integer type,@Param("start")  Date start, @Param("end") Date end);
      
      List<LuckyMoneyDetail> queryLuckyMoneyDetail(@Param("cid") Integer cid,@Param("type") Integer type,@Param("wechatid") String wechatid,@Param("start")  Date start, @Param("end") Date end);
}
