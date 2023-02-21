package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_chatgpt_conversation")
@Getter
@Setter
public class ChatGPTConversation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private String wechatid;
	private String friendid;
	private String conversation;
	@Column(name = "create_time")
	private Date createTime;
	 
}
