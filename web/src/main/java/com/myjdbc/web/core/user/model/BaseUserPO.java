package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.IDAutoGenerator;
import com.myjdbc.api.annotations.MyApiModel;
import com.myjdbc.api.annotations.MyApiModelProperty;
import com.myjdbc.api.annotations.MyID;
import lombok.Data;

/**
 * 基础用户实体
 *
 * @author 陈文
 * @date 2020/06/01  19:31
 */
@MyApiModel("USER")
@Data
public class BaseUserPO {

    /**
     * 唯一标识
     */
    @MyID(value = "唯一标识", name = "ID", type = IDAutoGenerator.Type.SNOW_FLAKE)
    private Integer id;

    /**
     * 用户名称
     */
    @MyApiModelProperty(value = "用户名称", name = "USER_NAME", required = true)
    private String userName;

    /**
     * 用户密码
     */
    @MyApiModelProperty(value = "用户密码", name = "USER_PASSWORD")
    private String userPassword;

}
