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

@Table(name = "tbl_accountinfo")
@Getter
@Setter
public class AccountInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;// AccountId
	private String account;
	private String password;
	private String nickname;
	private Integer type;// 账号类型（-1:超级用户，0:管理员，不能登录客户端；1：操作员，可登录客户端，运营后台暂时没有权限）（类型后续可扩展）
	private Integer cid;// SupplierId
	private Integer state;//当前账号状态1正常2禁用
	private Integer level;//等级1,2,3,4,5
	@Column(name = "create_time")
	private Date createTime;
  
	@Transient
	private String token;
}
