package com.myjdbc.api.model;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 注解对象
 *
 * @author game
 */
public class AnnotationObject {

    /**
     * 注解类
     */
    private Annotation annotation;

    /**
     * 解析的注解值
     */
    private Map<String, Object> values = new LinkedHashMap<>();

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public void addValue(String key, Object value) {
        values.put(key, value);
    }
}
