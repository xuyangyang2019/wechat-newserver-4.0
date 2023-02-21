package com.jubotech.business.web.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String wechatid;
	private String wechatno;
	private String wechatnick;
	private Integer count;
}
