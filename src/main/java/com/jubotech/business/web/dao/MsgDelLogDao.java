package com.jubotech.business.web.dao;

import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.MsgDelLog;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface MsgDelLogDao extends Mapper<MsgDelLog>, MySqlMapper<MsgDelLog>{}
