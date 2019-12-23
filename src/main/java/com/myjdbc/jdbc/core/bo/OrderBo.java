package com.myjdbc.jdbc.core.bo;

import com.myjdbc.jdbc.constants.OrderType;

/**
 * @Author 陈文
 * @Date 2019/12/8  16:50
 * @Description 本实体用于记录排序
 */
public class OrderBo {

    private String[] fieldNames;
    private OrderType orderType;

    public OrderBo(String[] fieldNames, OrderType orderType) {
        this.fieldNames = fieldNames;
        this.orderType = orderType;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public OrderType getOrderType() {
        return orderType;
    }

}
