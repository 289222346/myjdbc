package com.myjdbc.core.model;


import com.myjdbc.core.constants.OpType;

/**
 * 限定条件
 *
 * @author 陈文
 * @Date: 2020/4/18 12:28
 * @see Criteria 本类依赖于Criteria
 */
public class Criterion {

    /**
     * 限定条件
     */
    private final OpType op;
    /**
     * 限定值
     */
    private final Object fieldValue;

    /**
     * @param op         限定条件
     * @param fieldValue 限定值
     */
    public Criterion(OpType op, Object fieldValue) {
        this.op = op;
        this.fieldValue = fieldValue;
    }

    public OpType getOp() {
        return op;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
