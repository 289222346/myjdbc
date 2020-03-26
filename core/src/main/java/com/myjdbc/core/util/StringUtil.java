package com.myjdbc.core.util;

public class StringUtil {


    /**
     * 判断是否是空字符串 null和"" 都返回 true
     *
     * @param s
     * @return
     * @author Robin Chang
     */
    public static boolean isEmpty(String s) {
        if (s != null && !s.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        boolean flag = true;
        if (str != null && !str.equals("")) {
            if (str.toString().length() > 0) {
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * @Author 陈文
     * @Date 2019/12/18  11:14
     * @Description 从左边截取字符串
     */
    public static String leftSubstring(String str, int index) {
        return str.substring(0, index);
    }

    /**
     * @Author 陈文
     * @Date 2019/12/18  11:16
     * @Description 从右边截取字符串
     */
    public static String rightSubstring(String str, int index) {
        return str.substring(str.length() - index, str.length() - 1);
    }

}
