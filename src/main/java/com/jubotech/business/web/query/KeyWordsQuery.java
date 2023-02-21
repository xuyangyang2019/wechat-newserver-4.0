package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyWordsQuery extends BaseQuery {
	private Integer cid;
	private String keyWord;
	private String wechatid;
	
	/**
     * 匹配类型
     * 0精准完全匹配  1模糊包含匹配
     */
    private Integer keyType;
    /**
     * 资源类型
     */
    private Integer resourceType;
}
