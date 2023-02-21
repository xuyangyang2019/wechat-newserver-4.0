package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_commontaginfo")
@Getter
@Setter
public class CommontagInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String name;
	@Column(name = "create_time")
	private Date createTime;
}
