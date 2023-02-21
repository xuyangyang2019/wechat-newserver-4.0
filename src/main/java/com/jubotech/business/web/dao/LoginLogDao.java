package com.jubotech.business.web.dao;

import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.LoginLog;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface LoginLogDao extends Mapper<LoginLog>, MySqlMapper<LoginLog> {
}
