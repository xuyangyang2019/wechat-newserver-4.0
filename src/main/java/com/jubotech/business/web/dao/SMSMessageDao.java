package com.jubotech.business.web.dao;

import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.SMSMessage;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface SMSMessageDao extends Mapper<SMSMessage>, MySqlMapper<SMSMessage>{
}
