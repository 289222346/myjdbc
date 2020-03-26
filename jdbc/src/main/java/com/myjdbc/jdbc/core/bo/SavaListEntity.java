package com.myjdbc.jdbc.core.bo;

import java.util.List;

public class SavaListEntity {
    /**
     * 字段名
     */
    private String fieles;

    /**
     * 属性位置（?位置）
     */
    private String values;

    /**
     * 属性值
     */
    private List<Object[]> objs;

    /**
     * SQL语句
     */
    private String sql;

    public String getFieles() {
        return fieles;
    }

    public void setFieles(String fieles) {
        this.fieles = fieles;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public List<Object[]> getObjs() {
        return objs;
    }

    public void setObjs(List<Object[]> objs) {
        this.objs = objs;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
