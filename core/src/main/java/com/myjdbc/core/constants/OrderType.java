package com.myjdbc.core.constants;

/**
 * 数据库查询-关系运算符枚举
 *
 * @author 陈文
 * @date 2019/7/15 11:00
 */
@SuppressWarnings({"ALL", "AlibabaEnumConstantsMustHaveComment"})
public enum OrderType {

    /**
     * 升序排列
     */
    ASC(0, " ASC ", "升序排列"),
    /**
     * 降序排列
     */
    DESC(1, " DESC ", "降序排列");

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
