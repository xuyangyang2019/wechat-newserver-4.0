package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_accountinfo")
@Getter
@Setter
public class WxAccountInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private String wechatid;
	private String wechatno;
	private String wechatnick;
	private Integer isonline;//0在线 1下线
	private Integer cid;
	private Integer groupid;
	private String deviceid;//设备id（一般为imei）
	private String accountid;// 所属操作员账号id
	private Integer gender;
	private String avatar;
	private String country;
	private String province;
	private String city;
	private Integer snumber;//排序序号
	private Date login_time;
	private Date modify_time;
	 
	/////////////////// 新增/////////////////
	private String devnickname;// 设备名称
	private String brand;// 手机品牌
	private String module;// 手机型号
	
	@Transient
	private String cname;

	 
}
