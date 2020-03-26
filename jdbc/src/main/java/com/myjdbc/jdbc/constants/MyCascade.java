package com.myjdbc.jdbc.constants;

/**
 * 试题枚举常量
 *
 * @author KyrieCao
 * @date 2019/6/28 9:41
 */
public enum MyCascade {
    ONE_TO_ONE(1, "一对一"),
    ONE_TO_MANY(2, "一对多"),
    MANY_TO_ONE(4, "多对一"),
    TRANSIENT(8, "临时");
    private int code;
    private String remark;

    MyCascade(int code, String remark) {
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
