package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_timetask")
@Getter
@Setter
public class TaskTimeInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer accountid;
	
	@Column(name = "wechatid")
	private String wechatId;// 要执行的微信号
	private Integer cid;
	private Integer tasktype;// 任务类型1群发消息2发朋友圈
	private Integer state;// 状态  -1暂停     0已完成    1开启中     2取消
	private String execute_time;// 执行时间
	private Integer restype;// 资源类型(群发好友:0文字 1图片 ；朋友圈:0链接 2图片 3短视频)
	private String content;// 内容
	private String remarks;//任务备注
	private String remark2;//内容备份
	private Date update_time;
	@Column(name = "create_time")
	private Date createTime;
	
	////////////////////////
	@Transient
	private String account;
	@Transient
	private String nickname;
	
	// pc端定时传参///////////////////
	@Transient
	private String replace;//是否随机加表情
	
	@Transient
	private String comment;// 针对发朋友圈
	
	@Transient
	private String ids;
	
	@Transient
	private Integer attachtype;// 附件类型
	
	@Transient
	private String attachtcontent;// 附件内容
	
	@Transient
	private String whoinvisible;// 不给谁看
	  
	@Transient
	private String message;
	
	@Transient
	private String friendId;//好友微信ID
	@Transient
	private List<String> wechatList;// 要群发的微信号
	@Transient
	private Integer between_time;//群发间隔时间
	@Transient
	private Integer pageSize = 20;// 每页多少条
	@Transient
	private Integer pageNo = 1;// 当前第几页
 
}
