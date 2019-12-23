package com.myjdbc.core.util;

import java.util.List;

public final class ListUtil {

    /**
     * 是否是空集合
     *
     * @return boolean
     * @author ChenWen
     * @description
     * @date 2019/7/10 21:12
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    /**
     * 是否是非空集合
     *
     * @param list
     * @return boolean
     * @author ChenWen
     * @description
     * @date 2019/7/10 21:12
     */
    public static boolean isNotEmpty(List list) {
        return list != null && !list.isEmpty();
    }

}
