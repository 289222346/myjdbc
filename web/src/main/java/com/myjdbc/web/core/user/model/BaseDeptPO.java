package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.MyApiModel;
import com.myjdbc.api.annotations.MyApiModelProperty;

/**
 * 基础部门实体
 *
 * @author 陈文
 * @date 2020/06/01  20:36
 */
@MyApiModel(value = "DEPT", description = "部门")
public class BaseDeptPO extends BaseID {

    @MyApiModelProperty(value = "部门名称", name = "DEPT_NAME")
    private String deptName;

}
