package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_commonterminfo")
@Getter
@Setter
public class CommontermInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private Integer tid;
	private String name;
	private Integer ctype;//1 文本类型 2 图片类型 3 视频类型 4 语音类型 5 其他文件类型
	private String content;
	@Column(name = "create_time")
	private Date createTime;
	@Transient
	private String tname;
}
