package com.myjdbc.core.annotations;

import java.lang.annotation.*;

/**
 * ID自动生成器
 * <p>
 * 根据不同ID类型，自动生成唯一ID
 * {@link String}类型，使用{@link com.myjdbc.core.idgenerator.UUIDHexGenerator}生成
 * {@link Integer}类型，暂时不支持，准备使用雪花算法
 *
 * @Author 陈文
 * @Date 2020/4/23 19:12
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IDAutoGenerator {


    /**
     * ID的类型
     */
    Class<?> value();

}
