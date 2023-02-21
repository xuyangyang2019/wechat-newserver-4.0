package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_version_controls")
@Getter
@Setter
public class VersionControl implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String version;
	private Integer vernumber;
	private String packagename;
	private String packageurl;
	private Integer flag;//是否推送   0已推送   -1未推送
	@Column(name = "create_time")
	private Date createTime;
}
