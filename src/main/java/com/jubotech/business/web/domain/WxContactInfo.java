package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxContactInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;//
	private Integer cid;// 所属客户id
	private String wechatid;
	private String friendid;
	private String friend_wechatno;
	private String nickname;
	private String remark;
	private Integer gender;
	private String avatar;
	private String country;
	private String province;
	private String city;
	private Integer type;//0通讯录 1群聊
	private String memo;//备注
	private Date modify_time;
	private Date create_time;
}
