package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_timetask_details")
@Getter
@Setter
public class TimeTaskDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer tid;
	private String execute_time;//子任务执行时间
	private Integer state;//执行状态1未执行   0已执行   -1暂停
	private String json_content;// json数据
	private String msgid;
	private String results;// 手机端返回内容存储为json
	private Date create_time;
}
