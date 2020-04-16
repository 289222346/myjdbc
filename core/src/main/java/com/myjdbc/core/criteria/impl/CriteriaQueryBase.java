package com.myjdbc.core.criteria.impl;

import com.myjdbc.core.criteria.util.Restrictions;
import com.myjdbc.core.entity.Pag;
import com.myjdbc.core.util.ClassUtil;
import com.myjdbc.core.entity.Criteria;
import com.myjdbc.core.entity.Criterion;
import com.myjdbc.core.entity.OrderBo;
import com.myjdbc.core.constants.OrderType;
import com.myjdbc.core.criteria.CriteriaQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 查询条件构造器
 *
 * @author 陈文
 * @Description
 * @date 2019/7/15 9:47
 */
public class CriteriaQueryBase<T> implements CriteriaQuery {
    private static final Logger logger = LoggerFactory.getLogger(CriteriaQueryBase.class);

    //分页
    protected Pag pag;
    //查询实体
    protected final Class<T> cls;
    //查询条件
    protected final List<Criteria> criteriaList = new ArrayList<>();
    //排序条件
    protected OrderBo orderBo;

    public CriteriaQueryBase(Class<T> cls) {
        this.cls = cls;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  10:53
     * @Description 实体中的参数转换成eq参数
     */
    public CriteriaQueryBase(Class<T> cls, T t) {
        this(cls);
    }

    private void add(Criteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public void eq(String fieldName, Object value) {
        add(Restrictions.eq(fieldName, value));
    }

    @Override
    public void gt(String fieldName, Object value) {
        add(Restrictions.gt(fieldName, value));
    }

    @Override
    public void lt(String fieldName, Object value) {
        add(Restrictions.lt(fieldName, value));
    }

    @Override
    public void ge(String fieldName, Object value) {
        add(Restrictions.ge(fieldName, value));
    }

    @Override
    public void le(String fieldName, Object value) {
        add(Restrictions.le(fieldName, value));
    }

    @Override
    public void like(String fieldName, Object value) {
        add(Restrictions.like(fieldName, value));
    }

    @Override
    public void in(String fieldName, Object... value) {
        add(Restrictions.in(fieldName, value));
    }

    @Override
    public OrderBo getOrder() {
        return orderBo;
    }

    @Override
    public void setOrder(OrderType orderType, String... fieldNames) {
        orderBo = new OrderBo(fieldNames, orderType);
    }

    @Override
    public void eqProperty(String fieldName, String fieldName2) {
        add(Restrictions.eqProperty(fieldName, fieldName2));
    }

    @Override
    public Class getCls() {
        return cls;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/3  0:11
     * @Description 获取拼接的SQL查询条件
     */
    @Override
    public String getSqlString() {
        StringBuffer sqlBuff = new StringBuffer();
        for (Criteria criteria : criteriaList) {
            sqlBuff.append(criterionStr(criteria)).append(" AND ");
        }
        sqlBuff.append("1=1").append(getOrder());
        return sqlBuff.toString();
    }

    @Override
    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/3  0:12
     * @Description 获取辅助SQL查询的变量
     */
    @Override
    public Object[] getValues() {
        List<Object> list = new ArrayList<>();
        for (Criteria criteria : criteriaList) {
            if (criteria.getFieldValue() != null) {
                list.addAll(Arrays.asList(criteria.getFieldValue().toArray()));
            }
        }
        return list.toArray();
    }

    @Override
    public long getTotal() {
        return pag.getTotal();
    }

    @Override
    public void setTotal(long total) {
        pag.setTotal(total);
    }

    @Override
    public Pag getPag() {
        return pag;
    }

    @Override
    public void setPag(int page, int rows) {
        pag = new Pag(page, rows);
    }

    @Override
    public int getPage() {
        return pag.getPage();
    }

    @Override
    public int getRows() {
        return pag.getRows();
    }

    @Override
    public String getSql() {
        return pag.getSql();
    }

    @Override
    public void setSql(String sql) {
        pag.setSql(sql);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/2  21:17
     * @Description 拼接SQL字符串
     */
    private String criterionStr(Criteria Criteria) {
        Criterion criterion = Criteria.getCriterion();
        //主要字段名
        String fieldName = getColumnName(criterion.getFieldName());
        //运算符
        String remark = criterion.getOp().getValue();
        //被匹配字段名或值
        String succedaneum = Criteria.getFieldValue() != null ? criterion.getSuccedaneum() : getColumnName(criterion.getSuccedaneum());
        return " " + fieldName + remark + succedaneum + " ";
    }

    /**
     * @Author 陈文
     * @Date 2019/12/2  21:05
     * @Description 如果输入的属性名拥有Column注解，则取注解得name
     * 否则，取传入的值
     */
    private String getColumnName(String fieldName) {
        try {
            String getField = ClassUtil.getField(fieldName);
            Method getMethod = cls.getMethod(getField);
            if (getMethod != null) {
                Column column = getMethod.getAnnotation(Column.class);
                if (column != null) {
//                    return BeanUtil.getTableName(cls) + "." + column.name();
                } else {
                    JoinColumn joinColumn = getMethod.getAnnotation(JoinColumn.class);
                    if (joinColumn != null) {
//                        return BeanUtil.getTableName(cls) + "." + joinColumn.name();
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            logger.warn("NoSuchMethodException（没有这种方法）：" + e.getMessage());
        }
        return fieldName;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/8  16:53
     * @Description 拼接排序SQL
     */
//    private String getOrder() {
//        String str = "";
//        if (orderBo != null) {
//            for (String fieldName : orderBo.getFieldNames()) {
//                str += "," + getColumnName(fieldName);
//            }
//        }
//        if (!str.equals("")) {
//            str = str.substring(1);
//            str = " Order By " + str + orderBo.getOrderType().getValue();
//        }
//        return str;
//    }

//    /**
//     * @Author 陈文
//     * @Date 2019/12/8  16:54
//     * @Description 存入排序对象
//     */
//    private void addOrderBo(String fieldName, OrderType orderType) {
//        OrderBo orderBo = new OrderBo(fieldName, orderType);
//        if (this.orderBoList == null) {
//            this.orderBoList = new ArrayList<>();
//        }
//        this.orderBoList.add(orderBo);
//    }
}
