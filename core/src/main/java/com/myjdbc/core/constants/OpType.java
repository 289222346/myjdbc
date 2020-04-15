package com.myjdbc.core.constants;

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
    GT(3, " LIKE ", "模糊匹配"),
    LT(4, " LIKE ", "模糊匹配"),
    GE(5, " LIKE ", "模糊匹配"),
    LE(6, " LIKE ", "模糊匹配");

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
