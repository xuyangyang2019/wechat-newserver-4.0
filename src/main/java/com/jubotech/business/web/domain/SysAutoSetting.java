package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysAutoSetting implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer cid;
	private Integer auto_type;
	private String wechatId;
	private Integer state;//0开启  1未开启
	private String remarks;
	private Date create_time;
}
