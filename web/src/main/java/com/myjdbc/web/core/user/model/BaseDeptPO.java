package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.MyModel;
import com.myjdbc.api.annotations.MyModelProperty;

/**
 * 基础部门实体
 *
 * @author 陈文
 * @date 2020/06/01  20:36
 */
@MyModel(value = "DEPT", description = "部门")
public class BaseDeptPO extends BaseID {

    @MyModelProperty(value = "部门名称", name = "DEPT_NAME")
    private String deptName;

}
