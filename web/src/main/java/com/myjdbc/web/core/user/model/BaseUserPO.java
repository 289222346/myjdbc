package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.MyModel;
import com.myjdbc.api.annotations.MyModelProperty;
import lombok.Data;

/**
 * 基础用户实体
 *
 * @author 陈文
 * @date 2020/06/01  19:31
 */
@MyModel(value = "USER")
@Data
public class BaseUserPO extends BaseID {

    /**
     * 用户名称
     */
    @MyModelProperty(value = "用户名称", name = "USER_NAME", required = true)
    private String userName;

    /**
     * 用户密码
     */
    @MyModelProperty(value = "用户密码", name = "USER_PASSWORD", required = true)
    private String userPassword;


}
