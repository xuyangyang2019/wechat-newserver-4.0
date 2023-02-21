package com.jubotech.business.web.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuckyMoneyDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private String day;
	private Integer amount;
	private String wechatid;
	private String friendid;
	private String friendname;
	private Integer type;
}
