package com.myjdbc.api.model;

import com.myjdbc.api.annotations.MyApiModelProperty;

import java.lang.annotation.Annotation;

/**
 * 注解对象
 *
 * @author game
 */
public class MyApiModelPropertyAO implements MyApiModelProperty {

    private String value;

    private String name;

    private Boolean hidden;

    private Boolean required;

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean hidden() {
        return hidden;
    }

    @Override
    public boolean required() {
        return required;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

}
