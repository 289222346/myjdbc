package com.myjdbc.web.core.user.model;

import com.myjdbc.core.annotations.IDAutoGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 基础用户实体
 *
 * @author 陈文
 * @date 2020/06/01  19:31
 */
@IDAutoGenerator(type = IDAutoGenerator.Type.SNOW_FLAKE)
@ApiModel(value = "USER", description = "用户")
@Data
public class BaseUserPO {

    @Id
    @ApiModelProperty(value = "唯一标识", name = "ID")
    private Integer id;

    @ApiModelProperty(value = "用户名称", name = "USER_NAME", required = true)
    private String userName;

    @ApiModelProperty(value = "用户密码", name = "USER_PASSWORD")
    private String userPassword;

}
