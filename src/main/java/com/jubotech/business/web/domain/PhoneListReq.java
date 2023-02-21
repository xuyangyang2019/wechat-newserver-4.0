package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PhoneListReq implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer cid;
	private List<String> list;
}
