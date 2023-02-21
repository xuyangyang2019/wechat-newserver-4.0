package com.jubotech.business.web.domain.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CircleCommentVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private List<Integer> ids;
	private List<String> wechats;
	private List<String> comments;
}
