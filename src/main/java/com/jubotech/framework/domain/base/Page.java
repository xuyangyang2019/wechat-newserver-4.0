package com.jubotech.framework.domain.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {
	private Integer pageSize = 10;// 每页多少条

	private Integer pageNo = 1;// 当前第几页
}
