package com.myjdbc.jdbc.annotation;

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
     * @Description 表类型
     */
    Class tableClass();

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:10
     * @Description 非ID属性匹配时，需用指定属性名
     */
    String fieldName() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/10  16:12
     * @Description 要替换成的字段名（此处填写：数据库字段名）
     */
    String replaceName();

    /**
     * @Author 陈文
     * @Date 2019/12/11  22:25
     * @Description 数据库查询时，将本表指定属性赋值给该展示字段（此处填写：实体类属性名）
     */
    String copyName() default "";

    /**
     * @Author 陈文
     * @Date 2019/12/16  17:41
     * @Description 指定和字典值类型不一致的查询值类型
     */
    Class copyClass() default Class.class;
}