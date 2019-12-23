package com.myjdbc.jdbc.core.bo;

import java.util.List;

public class DeleteEntity {

    private List<String> sqlList;
    private Object pkValue;

    public List<String> getSqlList() {
        return sqlList;
    }

    public void setSqlList(List<String> sqlList) {
        this.sqlList = sqlList;
    }

    public Object getPkValue() {
        return pkValue;
    }

    public void setPkValue(Object pkValue) {
        this.pkValue = pkValue;
    }
}
