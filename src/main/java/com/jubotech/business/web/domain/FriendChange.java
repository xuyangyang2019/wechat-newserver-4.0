package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_wx_friendchange_log")
@Getter
@Setter
public class FriendChange implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private Integer accountid;
	private String wechatid;
    private Integer type;//功能  1增加好友  2删除好友
	private String friendid;
	@Column(name = "create_time")
	private Date createTime;
	 
}
