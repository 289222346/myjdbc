package com.myjdbc.test;

import com.myjdbc.util.DBUtil;
import com.myjdbc.util.DBconfig;

public class MyTest {


    public static void main(String[] args0) {
        /* 驱动 */
        String driver = "oracle.jdbc.OracleDriver";    //驱动标识符
        /* 数据库URL */
        String url = "jdbc:oracle:thin:@192.168.100.125:1521:GS";
        /* 用户 */
        String username = "GS";
        /* 密码 */
        String password = "root";
        /* 最大连接数 */
        int maxCount = 10;
        /* 最小连接数 */
        int minCount = 1;
        /* 关闭空闲连接扫描时间（分钟） */
        int closeTime = 5;
        //测试时配置数据库连接
        DBconfig dBconfig = new DBconfig(driver, url, username, password, maxCount, minCount, closeTime);
        DBUtil.setDbconfig(dBconfig);

        WExamRuleDao dao = new WExamRuleDao();

        //根据主键ID查找
//        WExamRule t = new WExamRule();
//        t = dao.findById("4028e4e86b6f5b94016b6f6d05060010");
//        System.out.println(t);

        //查找全部
//        List<WExamRule> list=dao.findAll();
//        System.out.println(list);

        //根据实体类中的参数查找
//        WExamRule t = new WExamRule();
//        t.setRuleName("期中考试");
//        List<WExamRule> list = dao.findAll(t);
//        System.out.println(list);

        //根据Map中的参数查找
//        Map<String, Object> map = new HashMap<>();
//        map.put("ruleName", "期中考试");
//        List<WExamRule> list = dao.findAll(map);
//        System.out.println(list);

//        t.setId("10000");
//        t.setText("测试");
//        Test t2=new Test();
//        t.setId("10001");
//        t.setText("测试2");
//        dao.saves(new Object[]{t,t2});
    }
}
