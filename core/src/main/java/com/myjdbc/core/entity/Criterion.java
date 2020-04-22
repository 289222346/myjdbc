package com.myjdbc.core.entity;


import com.myjdbc.core.constants.OpType;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Object> fieldValue = new ArrayList<>();

    /**
     * @param op         限定条件
     * @param fieldValue 限定值
     */
    public Criterion(OpType op, Object fieldValue) {
        this.op = op;
        this.fieldValue.add(fieldValue);
    }

    /**
     * @param op          限定条件
     * @param fieldValues 限定数组
     */
    public Criterion(OpType op, Object[] fieldValues) {
        this.op = op;
        if (fieldValues == null) {
            return;
        }
        for (Object fieldValue : fieldValues) {
            this.fieldValue.add(fieldValue);
        }
    }

    public OpType getOp() {
        return op;
    }

    public List<Object> getFieldValue() {
        return fieldValue;
    }
}
