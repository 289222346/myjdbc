package com.myjdbc.jdbc.constants;

/**
 * 数据库查询-关系运算符枚举
 *
 * @author 陈文
 * @date 2019/7/15 11:00
 */
public enum OrderType {
    ASC(0, " ASC ","正序排列"),
    DESC(1, " DESC ","倒序排列");

    private int code;
    private String value;
    private String remark;

    OrderType(int code, String value, String remark) {
        this.code = code;
        this.value = value;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
