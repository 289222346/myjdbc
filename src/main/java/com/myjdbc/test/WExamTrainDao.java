package com.myjdbc.test;

import com.myjdbc.sql.impl.BaseDaoOracle;
import com.myjdbc.util.PoolConnection;

public class WExamTrainDao extends BaseDaoOracle<WExamTrain> {

    protected WExamTrainDao(Class<WExamTrain> mainCls, String mainKey) {
        super(mainCls, mainKey);
    }

    protected WExamTrainDao(Class<WExamTrain> mainCls, String mainKey, PoolConnection conn) {
        super(mainCls, mainKey, conn);
    }

}
