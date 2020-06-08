package com.myjdbc.web.core.user.model;

import com.myjdbc.core.annotations.IDAutoGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;

/**
 * 用户与部门关联实体
 *
 * @author 陈文
 * @date 2020/06/01  20:39
 */
@IDAutoGenerator(type = IDAutoGenerator.Type.SNOW_FLAKE)
@ApiModel(value = "USER_AND_DEPT", description = "用户与部门")
public class UserAndDeptPO {

    @Id
    @ApiModelProperty(value = "唯一标识", name = "ID")
    private Integer id;

    @ApiModelProperty(value = "用户唯一标识", name = "USER_ID")
    private Integer userId;

    @ApiModelProperty(value = "部门唯一标识", name = "DEPT_ID")
    private Integer deptId;

}
