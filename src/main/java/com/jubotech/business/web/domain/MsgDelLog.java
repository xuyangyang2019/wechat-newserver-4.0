package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_msgdel_log")
@Getter
@Setter
public class MsgDelLog implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String wechatid;
	private String wechatno;
	private String nickname;
	private String friendid;
	private String friendnick;
	private String issend;
	private Integer contenttype;
	private String content;
	private String msgid;
	private String msgsvrid;
	@Column(name = "create_time")
	private Date createTime;
}
