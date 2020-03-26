package com.myjdbc.jdbc.constants;

/**
 * 数据库查询-关系运算符枚举
 *
 * @author 陈文
 * @date 2019/7/15 11:00
 */
public enum OpType {
    EQ(0, " = ", "完全相等"),
    IN(1, " IN ", "包含"),
    LIKE(2, " LIKE ", "模糊匹配"),
    ORDER_ASC(3, " ASC ", "正序排序");

    private int code;
    private String value;
    private String remark;

    OpType(int code, String value, String remark) {
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
