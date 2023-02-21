package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_wx_keywords")
@Getter
@Setter
public class KeyWords implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String wechatid;
	/**
     * 匹配类型
     * 0精准完全匹配  1模糊包含匹配
     */
    @Column(name = "key_type")
    private Integer keyType;
	@Column(name = "key_word")
	private String keyWord;
	@Column(name = "resource_type")
	private Integer resourceType;//根据聊天消息类型值一致
	@Column(name = "return_string")
	private String returnString;
	@Column(name = "create_time")
	private Date createTime;
	 
}
