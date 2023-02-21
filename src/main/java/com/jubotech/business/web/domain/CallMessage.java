package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_callmessage")
@Getter
@Setter
public class CallMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
    private String number;
    private Integer type;//根据proto类型
    private long date;
    private Integer duration;
    private String record;
    private String wechatid;
    private String imei;
    private Integer simid;
    private Integer blocktype;
	@Column(name = "create_time")
	private Date createTime;
	 
}
