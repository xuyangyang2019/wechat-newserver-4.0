package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CircleInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String sourcewechatid;
	private String wechatid;
	private String wechatnickname;
	private String circleid;
	private String content;
	private String publishtime;
	private String thumbimages;
	private String images;
	private String link;
	private String videothumbimg;
	private String videourl;
	private String videodescription;
	private String videomediaid;
	private Date createTime;
 
}
