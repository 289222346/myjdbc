package com.myjdbc.test;

import com.myjdbc.sql.impl.BaseDaoOracle;
import com.myjdbc.util.PoolConnection;

public class WExamRuleDao extends BaseDaoOracle<WExamRule> {

    protected WExamRuleDao() {
        super(WExamRule.class, "id");
    }

    protected WExamRuleDao(Class<WExamRule> mainCls, String mainKey, PoolConnection conn) {
        super(mainCls, mainKey, conn);
    }

}
