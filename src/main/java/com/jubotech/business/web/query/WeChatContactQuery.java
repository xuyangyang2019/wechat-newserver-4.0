package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class WeChatContactQuery extends BaseQuery {
	private Integer cid;
	private String wechatid;
	private String friend_wechatno;
	private String nickname;
	private Integer type;//0通讯录 1群聊
}
