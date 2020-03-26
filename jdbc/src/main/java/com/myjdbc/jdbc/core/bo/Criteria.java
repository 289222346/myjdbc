package com.myjdbc.jdbc.core.bo;

import java.util.List;

/**
 * @Author 陈文
 * @Date  2019/12/2  19:44
 * @Description 不加注释，反正加了你们也看不懂
 */
public class Criteria {

   private final Criterion criterion;
   private final List<Object> fieldValue;

    public Criteria(Criterion criterion, List<Object> fieldValue) {
       this.criterion = criterion;
       this.fieldValue = fieldValue;
   }

   public Criterion getCriterion() {
       return criterion;
   }

   public List<Object> getFieldValue() {
       return fieldValue;
   }
}
