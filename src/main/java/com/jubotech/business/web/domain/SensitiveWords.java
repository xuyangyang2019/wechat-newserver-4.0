package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 敏感词
 * 
 * @author lenovo
 *
 */
@Table(name = "tbl_sensitive_words")
@Getter
@Setter
public class SensitiveWords implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String words;
	private Date createTime;
}
