package com.myjdbc.test;

import com.myjdbc.sql.impl.BaseDaoOracle;
import com.myjdbc.util.PoolConnection;

public class WExamTrainDao extends BaseDaoOracle<Test> {

    protected WExamTrainDao() {
        super(Test.class, "id");
    }

    protected WExamTrainDao(Class<Test> mainCls, String mainKey, PoolConnection conn) {
        super(mainCls, mainKey, conn);
    }

}
