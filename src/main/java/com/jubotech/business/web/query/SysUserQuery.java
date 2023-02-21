package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

/**
 * @author SongFei
 * @date 2020/2/3 17:08
 */
@Getter
@Setter
public class SysUserQuery extends BaseQuery {

    private String userName;

    private String nickName;

}
