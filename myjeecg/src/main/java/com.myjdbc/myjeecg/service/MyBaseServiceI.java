package com.myjdbc.myjeecg.service;


import com.myjdbc.jdbc.core.service.BaseService;
import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;
import org.jeecgframework.core.common.model.json.DataGrid;

public interface MyBaseServiceI extends BaseService {

    void findAllToDataGrid(CriteriaQuery criteriaQuery, DataGrid dataGrid) ;

    void findAllToDataGrid(Class cls, DataGrid dataGrid, String sql, Object... values);

    void findAllToDataGrid(DataGrid dataGrid, String sql, Object... values);

}
