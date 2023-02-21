package com.jubotech.framework.common;

import com.jubotech.framework.util.StringUtil;

import lombok.Getter;
import lombok.Setter;

 
@Getter
@Setter
public class BaseQuery {

    private Integer page;

    private Integer rows;

    private String sord;

    private String sidx;
    
	private Integer getOffset() {
        return rows * (page - 1);
    }

    public String getSidx() {
        if (sidx == null) {
            return null;
        }
        return StringUtil.camelToUnderline(sidx);
    }

}
