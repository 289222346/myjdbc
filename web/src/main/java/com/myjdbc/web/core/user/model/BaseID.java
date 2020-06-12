package com.myjdbc.web.core.user.model;

import com.myjdbc.api.annotations.IDAutoGenerator;
import com.myjdbc.api.annotations.MyID;
import lombok.Data;

/**
 * 基础用户实体
 *
 * @author 陈文
 * @date 2020/06/01  19:31
 */
@Data
public class BaseID {

    /**
     * 唯一标识
     */
    @MyID(type = IDAutoGenerator.Type.SNOW_FLAKE)
    private Integer id;

}
