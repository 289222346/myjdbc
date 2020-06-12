package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.IDAutoGenerator;
import com.myjdbc.api.annotations.MyApiModelProperty;
import org.springframework.data.annotation.Id;

/**
 * 基础部门实体
 *
 * @author 陈文
 * @date 2020/06/01  20:36
 */
@IDAutoGenerator(type = IDAutoGenerator.Type.SNOW_FLAKE)
//@ApiModel(value = "DEPT", description = "部门")
public class BaseDeptPO {

    @Id
    @MyApiModelProperty(value = "唯一标识", name = "ID")
    private Integer id;

    @MyApiModelProperty(value = "部门名称", name = "DEPT_NAME")
    private String deptName;

}
