package com.myjdbc.core.model;


/**
 * 扩展表
 *
 * @author 陈文
 */
public class ExtenTable {

    /**
     * 扩展表名
     */
    private String table;
    /**
     * 原表关联字段
     */
    private String localField;
    /**
     * 扩展表被关联字段
     */
    private String foreignField;
    /**
     * 结果别名
     */
    private String as;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getLocalField() {
        return localField;
    }

    public void setLocalField(String localField) {
        this.localField = localField;
    }

    public String getForeignField() {
        return foreignField;
    }

    public void setForeignField(String foreignField) {
        this.foreignField = foreignField;
    }

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
    }
}
