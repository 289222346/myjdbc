package com.myjdbc.mymongodb;


import io.swagger.annotations.Extension;
import io.swagger.annotations.ExtensionProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 中文注释
 *
 * @Author 陈文
 * @Date 2020/4/26  9:05
 * <p>
 * <p>
 * Copyright 2016SmartBear Software
 * <p>
 * 根据Apache许可版本2.0（“许可”）获得许可;
 * 除非遵守许可，否则不得使用此文件.
 * 您可以在以下位置获得许可证的副本
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * 除非适用法律要求或以书面形式同意，
 * 否则根据“许可”分发的软件将按“原样”分发，
 * 没有任何形式的明示或暗示担保或条件。
 * 有关许可下特定的语言管理权限和限制，请参见许可
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyApiModelProperty {

    /**
     * 此属性的简要说明.
     */
    String value() default "";

    /**
     * 允许覆盖属性名称（字段的别名）
     *
     * @return 覆盖的属性名称（字段的别名）
     */
    String name() default "";

    /**
     * 限制此参数的可接受值.
     * <p>
     * 有三种描述允许值的方法:
     * <ol>
     * <li>要设置值列表，请提供一个逗号分隔的列表.
     * 例如: {@code first, second, third}.</li>
     * <li>要设置值的范围，请以“范围”开始该值，并用方括号括起来包括最小值和最大值，
     * 或者用圆括号括起来以表示唯一的最小值和最大值。
     * For example: {@code range[1, 5]}, {@code range(1, 5)}, {@code range[1, 5)}.</li>
     * <li>要设置最小值/最大值，请对范围使用相同的格式，但将“ infinity”或“ -infinity”用作第二个值。
     * 例如，{@code range[1, infinity]} 表示此参数的最小允许值为1.</li>
     * </ol>
     */
    String allowableValues() default "";

    /**
     * 允许从API文档中过滤属性。请参阅io.swagger.core.filter.SwaggerSpecFilter。
     */
    String access() default "";

    /**
     * 目前未使用.
     */
    String notes() default "";

    /**
     * 参数的数据类型.
     * 可以重写属性类型
     * <p>
     * 这可以是类名或原语。该值将覆盖从class属性读取的数据类型.
     */
    String dataType() default "";

    /**
     * 指定是否需要该参数
     * {@code true}必填
     * {@code false}非必填
     */
    boolean required() default false;

    /**
     * 允许在模型中显式排序属性.
     */
    int position() default 0;

    /**
     * 是否隐藏该属性,让该属性不参与数据库操作.
     * {@code true}隐藏
     * {@code false}不隐藏
     */
    boolean hidden() default false;

    /**
     * 举例说明.
     */
    String example() default "";

    /**
     * 允许将模型属性指定为只读.
     *
     * @deprecated As of 1.5.19, replaced by {@link #accessMode()}
     */
    @Deprecated
    boolean readOnly() default false;

    /**
     * 允许指定模型属性的访问模式 ({@link AccessMode#READ_ONLY},{@link AccessMode#READ_WRITE)})
     *
     * @since 1.5.19
     */
    AccessMode accessMode() default AccessMode.AUTO;


    /**
     * Specifies a reference to the corresponding type definition, overrides any other metadata specified
     */

    String reference() default "";

    /**
     * Allows passing an empty value
     *
     * @since 1.5.11
     */
    boolean allowEmptyValue() default false;

    /**
     * @return an optional array of extensions
     */
    Extension[] extensions() default @Extension(properties = @ExtensionProperty(name = "", value = ""));

    enum AccessMode {
        AUTO,
        READ_ONLY,
        READ_WRITE;
    }
}
