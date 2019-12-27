package com.myjdbc.jdbc.core.service.impl;


import com.myjdbc.jdbc.constants.OpType;
import com.myjdbc.jdbc.core.bo.Criteria;
import com.myjdbc.jdbc.core.bo.Criterion;

import java.util.Arrays;

public class Restrictions {

    public static Criteria eq(String fieldName, Object value) {
//        Criterion criterion = new Criterion(fieldName, "?", OpType.EQ);
//        List<Object> list = new ArrayList<>();
//        list.add(value);
//        Criteria criteria = new Criteria(criterion, list);
        return getCriteria(fieldName, OpType.EQ, value);
    }

    public static Criteria eqProperty(String fieldName, String fieldName2) {
        Criterion criterion = new Criterion(fieldName, fieldName2 + "", OpType.EQ);
        Criteria criteria = new Criteria(criterion, null);
        return criteria;
    }

    public static Criteria in(String fieldName, Object... values) {
//        Criterion criterion = new Criterion(fieldName, "(" + getSuccedaneum(values.length) + ")", OpType.IN);
//        Criteria criteria = new Criteria(criterion, Arrays.asList(values));
        return getCriteria(fieldName, OpType.IN, values);
    }

    public static Criteria like(String fieldName, Object... values) {
//        Criterion criterion = new Criterion(fieldName, "(" + getSuccedaneum(values.length) + ")", OpType.LIKE);
//        Criteria criteria = new Criteria(criterion, Arrays.asList(values));
        return getCriteria(fieldName, OpType.LIKE, values);
    }

    public static String getSuccedaneum(int i) {
        StringBuffer succedaneum = new StringBuffer();
        for (int j = 0; j < i; j++) {
            succedaneum.append("?,");
        }
        if (succedaneum.length() > 0) {
            succedaneum.deleteCharAt(succedaneum.length() - 1);
        }
        return succedaneum.toString();
    }

    private static Criteria getCriteria(String fieldName, OpType opType, Object... values) {
        int length = getLength(values, 0);
        Criterion criterion = new Criterion(fieldName, "(" + getSuccedaneum(length) + ")", opType);
        Criteria criteria = new Criteria(criterion, Arrays.asList(values));
        return criteria;
    }

    private static int getLength(Object[] values, int i) {
        for (Object value : values) {
            if (value != null) {
                if (value.getClass().isArray()) {
                    //如果是数组类型，则继续解析他
                    i += getLength((Object[]) value, i);
                } else {
                    i++;
                }
            }
        }
        return i;
    }


}
