package com.jubotech.framework.domain.base;

import java.util.Map;

import com.jubotech.framework.common.ActionResult;

import lombok.Getter;
import lombok.Setter;

/**
 * 全局返回封装类
 */
@Getter
@Setter
public class Resp {

	private Integer bizCode = ActionResult.BIZCODE_SUCCESS;

	private String msg = ActionResult.SUCCESS_MSG;

	private Map<String, Object> data;

	public Resp() {
		super();
	}

	public Resp(Map<String, Object> data) {
		super();
		this.data = data;
	}

	public Resp(Integer bizCode, String msg, Map<String, Object> data) {
		super();
		this.bizCode = bizCode;
		this.msg = msg;
		this.data = data;
	}
}
