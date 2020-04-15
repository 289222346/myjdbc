package com.myjdbc.core.criteria.util;


import com.myjdbc.core.constants.OpType;
import com.myjdbc.core.entity.Criteria;
import com.myjdbc.core.entity.Criterion;

import java.util.Arrays;

/**
 * 限制条件标准生成器
 *
 * @Author 陈文
 * @Date 2020/4/15  11:31
 * @return
 */
public class Restrictions {

    public static Criteria eq(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.EQ, value);
    }

    public static Criteria gt(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.GT, value);
    }

    public static Criteria lt(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.LT, value);
    }

    public static Criteria ge(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.GE, value);
    }

    public static Criteria le(String fieldName, Object value) {
        return getCriteria(fieldName, OpType.LE, value);
    }

    public static Criteria eqProperty(String fieldName, String fieldName2) {
        Criterion criterion = new Criterion(fieldName, fieldName2 + "", OpType.EQ);
        Criteria criteria = new Criteria(criterion, null);
        return criteria;
    }

    public static Criteria in(String fieldName, Object... values) {
        return getCriteria(fieldName, OpType.IN, values);
    }

    public static Criteria like(String fieldName, Object... values) {
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
