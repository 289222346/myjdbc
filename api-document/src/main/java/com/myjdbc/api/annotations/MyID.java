package com.myjdbc.api.annotations;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author 陈文
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@IDAutoGenerator
@MyApiModelProperty(required = true)
public @interface MyID {

    /**
     * 属性的描述
     */
    @AliasFor(attribute = "value", annotation = MyApiModelProperty.class)
    String value() default "";

    /**
     * 属性的别名（数据库名称）
     */
    @AliasFor(attribute = "name", annotation = MyApiModelProperty.class)
    String name() default "";


    /**
     * ID自动生成器工作方式
     */
    @AliasFor(attribute = "name", annotation = IDAutoGenerator.class)
    IDAutoGenerator.Type type() default IDAutoGenerator.Type.DEFAULT;


}
