package com.jubotech.business.web.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jubotech.business.web.domain.CircleComment;
import com.jubotech.business.web.query.CircleCommentQuery;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Repository
public interface CircleCommentDao extends Mapper<CircleComment>, MySqlMapper<CircleComment> {
	List<CircleComment> pageList(CircleCommentQuery query);
	List<CircleComment> pageList1(CircleCommentQuery query);
}
