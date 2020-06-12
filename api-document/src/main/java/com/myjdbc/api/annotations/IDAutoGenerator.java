package com.myjdbc.api.annotations;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;


/**
 * ID自动生成器
 * <p>
 * 根据不同ID类型，自动生成唯一ID
 * {@link String}类型，使用UUID生成
 * {@link Integer}类型，使用雪花算法生成64位二进制数字
 * <p>
 * 默认为{@link String}类型,若类型不支持，也视为使用默认类型
 *
 * @Author 陈文
 * @Date 2020/4/23 19:12
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IDAutoGenerator {

    /**
     * （1.4.6以后不建议使用，若要指定类型，请指定type）
     * 自动生成的ID类型
     *
     * @return
     */
    @Deprecated
    Class<?> value() default String.class;

    /**
     *
     */
    Type type() default Type.DEFAULT;

    enum Type {
        DEFAULT(String.class, "默认，1.4.6之前作为兼容类型，以value为准"),
        UUID(String.class, "UUID算法"),
        SNOW_FLAKE(Integer.class, "SnowFlake(雪花)算法");

        /**
         * ID类型
         */
        private final Class cls;

        Type(Class cls, String desc) {
            this.cls = cls;
        }

        public Class getCls() {
            return cls;
        }
    }

}
