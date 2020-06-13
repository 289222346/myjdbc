package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.MyModel;
import com.myjdbc.api.annotations.MyModelProperty;

/**
 * 用户与部门关联实体
 *
 * @author 陈文
 * @date 2020/06/01  20:39
 */
@MyModel(value = "USER_AND_DEPT", description = "用户与部门")
public class UserAndDeptPO extends BaseID {

    @MyModelProperty(value = "用户唯一标识", name = "USER_ID")
    private Integer userId;

    @MyModelProperty(value = "部门唯一标识", name = "DEPT_ID")
    private Integer deptId;

}
