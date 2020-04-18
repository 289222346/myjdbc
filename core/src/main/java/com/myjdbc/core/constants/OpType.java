package com.myjdbc.core.constants;

/**
 * 数据库查询-关系运算符枚举
 *
 * @author 陈文
 * @date 2019/7/15 11:00
 */
@SuppressWarnings({"ALL", "AlibabaEnumConstantsMustHaveComment"})
public enum OpType {
    /**
     * 完全相等
     */
    EQ(0, "完全相等"),
    /**
     * 包含
     */
    IN(1, "包含"),
    /**
     * 模糊匹配
     */
    LIKE(2, "模糊匹配"),
    /**
     * 大于"
     */
    GT(3, "大于"),
    /**
     * 小于
     */
    LT(4, "小于"),
    /**
     * 大于等于
     */
    GE(5, "大于等于"),
    /**
     * 小于等于
     */
    LE(6, "小于等于"),
    /**
     * 属性匹配
     */
    EQ_PROPERTY(7, "属性匹配");

    /**
     * 运算符编码
     */
    private int code;
    /**
     * 运算符备注
     */
    private String remark;

    OpType(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
