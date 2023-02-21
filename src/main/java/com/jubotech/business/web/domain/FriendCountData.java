package com.jubotech.business.web.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendCountData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String time;
	private Integer count;
	private Integer snumber;
	private String  wechatid;
	private String  nickname;
}
