package com.jubotech.business.web.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tongji implements Serializable{
	private static final long serialVersionUID = 1L;
	private String day;
	private Integer count;
}
