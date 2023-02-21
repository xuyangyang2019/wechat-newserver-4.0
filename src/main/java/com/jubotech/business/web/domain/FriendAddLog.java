package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_friendadd_log")
@Getter
@Setter
public class FriendAddLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private Integer groupid;
	private Integer count;
	private String wechatid;
	private String nickname;
	private Integer snumber;
	@Column(name = "create_time")
	private Date createTime;
	 
}
