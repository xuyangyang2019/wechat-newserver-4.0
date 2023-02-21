package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_resources")
@Getter
@Setter
public class Resources implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private Integer type;//5emoji表情素材，4小程序素材，3链接素材，2发朋友圈素材，1群发素材
	private String remarks;
	private String content;
	@Column(name = "create_time")
	private Date createTime;
	 
}
