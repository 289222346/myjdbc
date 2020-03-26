package com.myjdbc.jdbc.core.bo;


import com.myjdbc.jdbc.constants.OpType;

/**
 * 查询条件SQL
 * （参数用?号替代，需要配合Criteria使用）
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:48
 * @see Criteria 查询时实际使用Criteria类，本类用于生成SQL,值保存在Criteria
 */
public class Criterion {

    private final String fieldName;
    private final String succedaneum;
    private final OpType op;

    public Criterion(String fieldName, String succedaneum, OpType op) {
        this.fieldName = fieldName;
        this.succedaneum = succedaneum;
        this.op = op;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getSuccedaneum() {
        return succedaneum;
    }

    public OpType getOp() {
        return op;
    }
}
