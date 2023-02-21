package com.jubotech.framework.domain.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DBPage {
	// 数据库用
	private Integer limit;

	private Integer offset;

	public Integer getLimit() {
		return limit;
	}

	public DBPage(Integer limit, Integer offset) {
		super();
		this.limit = limit;
		this.offset = offset;
	}
}
