package com.myjdbc.myjeecg.service.impl;

import com.myjdbc.core.util.FileUtil;
import com.myjdbc.myjeecg.service.MyBaseServiceDao;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.p3.core.logger.Logger;
import org.jeecgframework.p3.core.logger.LoggerFactory;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MyBaseServiceDaoImpl extends com.myjdbc.myjeecg.service.impl.MyBaseServiceImpl implements MyBaseServiceDao {
    private static final Logger logger = LoggerFactory.getLogger(MyBaseServiceDaoImpl.class);

    @Autowired
    SystemService systemService;

    @Override
    public <T> void findSqlToDataGrid(Class<T> cls, String sqlFileName, DataGrid dataGrid, Object... values) {
        String sql = getSql(sqlFileName, values);
        //将*替换成%
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String valueStr = values[i] + "";
                values[i] = valueStr.replace('*', '%');
            }
        }
        findAllToDataGrid(cls, dataGrid, sql, values);
    }

    @Override
    public void findSqlToDataGrid(String sqlFileName, DataGrid dataGrid, Object... values) {
        String sql = getSql(sqlFileName, values);
        //将*替换成%
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String valueStr = values[i] + "";
                values[i] = valueStr.replace('*', '%');
            }
        }
        findAllToDataGrid(dataGrid, sql, values);
    }

    @Override
    public void findSqlToDataGrid(String sqlFileName, DataGrid dataGrid, Map<String, Object> map) {
        Map<String, Object> rMap = getSql(sqlFileName, map);
        String sql = rMap.get("sql") + "";
        Object v = rMap.get("values");
        Object[] values = null;
        if (v != null) {
            values = (Object[]) v;
        }
        //将*替换成%
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String valueStr = values[i] + "";
                values[i] = valueStr.replace('*', '%');
            }
        }
        findAllToDataGrid(dataGrid, sql, values);
    }

    @Override
    public <T> List<T> findSqlFile(Class<T> cls, String sqlFileName, Map<String, Object> map) {
        Map<String, Object> rMap = getSql(sqlFileName, map);
        String sql = rMap.get("sql") + "";
        Object v = rMap.get("values");
        Object[] values = null;
        if (v != null) {
            values = (Object[]) v;
        }

        //将*替换成%
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String valueStr = values[i] + "";
                values[i] = valueStr.replace('*', '%');
            }
        }
        return findAll(cls, sql, values);
    }


    @Override
    public <T> List<T> findSqlFile(Class<T> cls, String sqlFileName, Object... values) {
        String sql = getSql(sqlFileName, values);
        //将*替换成%
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null) {
                String valueStr = values[i] + "";
                values[i] = valueStr.replace('*', '%');
            }
        }
        return findAll(cls, sql, values);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  23:59
     * @Description 从文件中获取SQL，并根据传参解析
     */
    private String getSql(String sqlFileName, Object[] values) {
        StringBuffer sql = getSql(sqlFileName);
        int i = 0;
        for (Object value : values) {
            i = sql.indexOf("[", i);
            if (i != -1) {
                if (value != null && !value.equals("")) {
                    //去除[]括号
                    sql.deleteCharAt(sql.indexOf("[", i));
                    sql.deleteCharAt(sql.indexOf("]", i));
                } else {
                    //去除[]之间的条件
                    sql.delete(sql.indexOf("[", i), sql.indexOf("]", i) + 1);
                }
            }
        }
        return sql + "";
    }

    /**
     * @Author 陈文
     * @Date 2019/12/10  23:59
     * @Description 从文件中获取SQL，并根据传参解析
     */
    private Map<String, Object> getSql(String sqlFileName, Map<String, Object> map) {
        StringBuffer sql = getSql(sqlFileName);
        List<Object[]> parameterList = new ArrayList<>();
        //遍历参数
        for (String key : map.keySet()) {
            //查找该参数是否存在SQL中
            int index = sql.indexOf(":" + key);
            while (index != -1) {
                //替换参数为占位符?,并且将参数写入参数Map
                sql.replace(index, index + key.length() + 1, "?");
                parameterList.add(new Object[]{key, index});
                index = sql.indexOf(":" + key);
            }
        }

        for (int i = 0; i < parameterList.size(); i++) {
            for (int j = 0; j < parameterList.size() - 1 - i; j++) {
                if ((Integer) parameterList.get(j + 1)[1] < (Integer) parameterList.get(j)[1]) {
                    Object[] temp = parameterList.get(j + 1);
                    parameterList.set(j + 1, parameterList.get(j));
                    parameterList.set(j, temp);
                }
            }
        }
        Object[] values = new Object[parameterList.size() + 1];
        for (int i = 0; i < parameterList.size(); i++) {
            values[i] = map.get(parameterList.get(i)[0]);
        }
        Map<String, Object> rMap = new HashMap<>();
        rMap.put("sql", sql + "");
        rMap.put("values", values);
        return rMap;
    }

    private StringBuffer getSql(String sqlFileName) {
        StringBuffer path = new StringBuffer(this.getClass().getResource("").getPath());
        path.delete(path.lastIndexOf("/", path.length() - 2) + 1, path.length());
        path.delete(path.lastIndexOf("/", path.length() - 2) + 1, path.length());
        path.append("sql/").append(sqlFileName).append(".sql");
        return FileUtil.getSql(path + "");
    }

    /**
     * 使用JEECG自带的日志写入
     */
    @Override
    public void addLog(String logcontent, Short operatetype, Short loglevel) {
        systemService.addLog(logcontent, operatetype, loglevel);
    }

}
