package com.jubotech.framework.domain.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PageBean<T> {
	private Integer pageSize;//每页多少条

	private Integer pageNo;//当前第几页

	private Integer pageTotal;//总共多少页

	private List<T> resultList;
	
	private String weChatId;
	 
	public PageBean(Integer pageSize, Integer pageNo, Integer pageTotal, List<T> resultList) {
		super();
		this.pageSize = pageSize;
		this.pageNo = pageNo;
		this.pageTotal = pageTotal;
		this.resultList = resultList;
	}
}
