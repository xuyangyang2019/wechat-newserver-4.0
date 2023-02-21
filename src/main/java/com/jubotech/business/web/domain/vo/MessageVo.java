package com.jubotech.business.web.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MessageVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String ContentType;
	private String Content;
}
