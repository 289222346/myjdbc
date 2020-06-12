package com.myjdbc.api.model;

import com.myjdbc.api.annotations.IDAutoGenerator;

import java.lang.annotation.Annotation;

/**
 * 注解对象
 *
 * @author game
 */
public class IDAutoGeneratorAO implements IDAutoGenerator {

    private String value;

    private Type type;

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Class<?> value() {
        return null;
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
