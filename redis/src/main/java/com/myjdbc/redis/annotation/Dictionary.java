package com.myjdbc.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @Author 陈文
 * @Date 2019/12/10  17:35
 * @Description 注解只对String类型有效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dictionary {

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:10
     * @Description 要查询的数据库表
     */
    Class tableClass();

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:10
     * @Description 查询条件，非ID属性匹配时，需用指定属性名。该属性表示查询条件，为table中的某个字段名
     */
    String fieldName() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:12
     * @Description 要展示的字段名（此处填写：table数据库中的字段名）
     */
    String replaceName();

    /**
     * @Author 陈文
     * @Date 2019/12/11  22:25
     * @Description 数据库查询时，将使用注解的对象中名称为copyName的指定属性，赋值给使用注解的属性（此处填写：实体类属性名）
     */
    String copyName() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/16  17:41
     * @Description 指定和字典值类型不一致的查询值类型（2020.03.02暂时未使用）
     */
    Class copyClass() default Class.class;
}