package com.myjdbc.jdbc.core.bo;

import java.util.List;

/**
 * 保存时需要用到的临时数据
 *
 * @author ChenWen
 * @Description 用于保存解析实体时需要用到的多个数据
 * @date 2019/7/12 14:20
 */
public class SavaEntity {
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
    private List objs;

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

    public List getObjs() {
        return objs;
    }

    public void setObjs(List objs) {
        this.objs = objs;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
