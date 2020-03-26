package com.myjdbc.myjeecg.service.impl;


import com.myjdbc.core.service.BaseServiceImpl;
import com.myjdbc.jdbc.constants.OrderType;
import com.myjdbc.jdbc.core.sqlcriteria.CriteriaQuery;
import com.myjdbc.jdbc.core.sqlcriteria.impl.CriteriaQueryOracle;
import com.myjdbc.myjeecg.service.MyBaseServiceI;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.p3.core.logger.Logger;
import org.jeecgframework.p3.core.logger.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("myBaseService")
public class MyBaseServiceImpl extends BaseServiceImpl implements MyBaseServiceI {
    private static final Logger logger = LoggerFactory.getLogger(MyBaseServiceImpl.class);

    @Override
    public void findAllToDataGrid(CriteriaQuery criteriaQuery, DataGrid dataGrid)  {
        criteriaQuery.setPag(dataGrid.getPage(), dataGrid.getRows());
        orderData(criteriaQuery, dataGrid);
        List list = super.findAll(criteriaQuery);
        if (list == null) {
            list = new ArrayList();
        }
        dataGrid.setResults(list);
        dataGrid.setTotal(criteriaQuery.getTotal());
    }

    private void orderData(CriteriaQuery criteriaQuery, DataGrid dataGrid) {
        String sort = dataGrid.getSort();
        String order = dataGrid.getOrder();
        OrderType orderType = null;
        if (StringUtil.isNotEmpty(order)) {
            if (order.equals("asc")) {
                orderType = OrderType.ASC;
            } else {
                orderType = OrderType.DESC;
            }
        }
        if (StringUtil.isNotEmpty(sort) && orderType != null) {
            criteriaQuery.setOrder(orderType, sort);
        }
    }

    @Override
    public void findAllToDataGrid(Class cls, DataGrid dataGrid, String sql, Object... values) {
        CriteriaQuery criteriaQuery = new CriteriaQueryOracle(cls);
        criteriaQuery.setPag(dataGrid.getPage(), dataGrid.getRows());
        findAllToDataGrid(criteriaQuery, dataGrid, sql, values);
    }

    @Override
    public void findAllToDataGrid(DataGrid dataGrid, String sql, Object... values) {
        CriteriaQuery criteriaQuery = new CriteriaQueryOracle(Map.class);
        criteriaQuery.setPag(dataGrid.getPage(), dataGrid.getRows());
        findAllToDataGrid(criteriaQuery, dataGrid, sql, values);
    }

    private void findAllToDataGrid(CriteriaQuery criteriaQuery, DataGrid dataGrid, String sql, Object... values) {
        List list = super.findAll(criteriaQuery, sql, values);
        if (list == null) {
            list = new ArrayList();
        }

        dataGrid.setResults(list);
        dataGrid.setTotal(criteriaQuery.getTotal());
    }
}
