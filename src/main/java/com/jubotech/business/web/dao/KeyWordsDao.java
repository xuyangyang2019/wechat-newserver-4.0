package com.jubotech.business.web.dao;

import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.KeyWords;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface KeyWordsDao extends Mapper<KeyWords>, MySqlMapper<KeyWords>{
}
