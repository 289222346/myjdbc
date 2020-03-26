package com.myjdbc.myjeecg.service;

import org.jeecgframework.core.common.model.json.DataGrid;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MyBaseServiceDao extends MyBaseServiceI {


    <T> void findSqlToDataGrid(Class<T> cls, String sqlFileName, DataGrid dataGrid, Object... values);


    /**
     * @Author 陈文
     * @Date 2019/12/15  20:22
     * @Description 不推荐使用Map作为返回值
     */
    @Deprecated
    void findSqlToDataGrid(String sqlFileName, DataGrid dataGrid, Object... values);

    @Deprecated
    void findSqlToDataGrid(String sqlFileName, DataGrid dataGrid, Map<String, Object> map);


    <T> List<T> findSqlFile(Class<T> cls, String sqlFileName, Object... values) throws SQLException;

    <T> List<T> findSqlFile(Class<T> cls, String sqlFileName, Map<String, Object> map) throws SQLException;


    /**
     * 日志添加
     *
     * @param LogContent  内容
     * @param loglevel    级别
     * @param operatetype 类型
     */
    public void addLog(String LogContent, Short operatetype, Short loglevel);

}
