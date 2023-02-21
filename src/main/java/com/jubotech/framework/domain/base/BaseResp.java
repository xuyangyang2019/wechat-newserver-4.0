package com.jubotech.framework.domain.base;

import java.io.Serializable;
import java.util.Map;

import com.jubotech.framework.util.ActionResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResp implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer bizCode = ActionResult.BIZCODE_SUCCESS;

	private String msg = "success";

	private Map<String, Object> data;
 
}
