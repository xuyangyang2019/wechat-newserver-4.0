package com.jubotech.business.web.domain.vo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskTimeVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer cid;
	private Integer accountid;//客服id
	private Integer tasktype;// 任务类型：1群发消息，2发朋友圈，3删除好友，4删除并退出群聊，5群发群
	private Integer state;// 状态1开启中0已完成
	private String execute_time;// 执行时间
	private Integer restype;// 资源类型(群发好友:1文字 2图片 ；朋友圈:0链接 2图片 3短视频)
	private String content;// 内容
	private String comment;// 针对发朋友圈
	private String remarks;//任务备注
	private Integer attachtype;// 附件类型
	private String attachtcontent;// 附件内容
	private String whoinvisible;// 不给谁看
  
	// pc端定时传参///////////////////
	@Transient
	private String replace;//是否随机加表情
	@Transient
	private String message;
	@Transient
	private String wechatId;// 要执行的微信号
	@Transient
	private String friendId;//好友微信ID
	@Transient
	private List<String> wechatList;// 要群发的微信号
	@Transient
	private Integer between_time;//群发间隔时间
 
}
