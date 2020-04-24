package com.myjdbc.core.annotations;

import java.lang.annotation.*;


/**
 * 数据传输对象DTO（data transfer object）
 * 拥有该注解的类，所有属性会被当做其他实体被解析
 * 此类可用于多表联合查询
 *
 * @Author 陈文
 * @Date 2020/4/24  11:28
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface DTO {

}
