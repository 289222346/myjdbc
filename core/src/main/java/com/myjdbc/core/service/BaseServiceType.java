package com.myjdbc.core.service;

/**
 * 操作结果状态码
 *
 * @Author 陈文
 * @Date 2020/4/11  17:32
 */
public interface BaseServiceType {

    /**
     * 操作结果：操作成功
     */
    int SUCCESS = 0;
    /**
     * 操作结果：操作失败，没有ApiModel注解
     */
    int FAILURE_LACK_MODEL = 1;
    /**
     * 操作结果：操作失败，ID冲突
     */
    int FAILURE_ID_CLASH = 2;
    /**
     * 操作结果：操作失败，对象所有属性为空
     */
    int FAILURE_ALL_NULL = 3;
    /**
     * 操作结果：操作失败，ID属性为空
     */
    int FAILURE_ID_NULL = 4;
    /**
     * 操作结果：操作失败，内部错误
     */
    int FAILURE_INSIDE_ERROR = 5;
    /**
     * 操作结果：操作失败，不允许传入集合
     */
    int FAILURE_NO_LIST = 6;
    /**
     * 操作结果：操作失败，找不到没有数据
     */
    int FAILURE_NO_DATA = 7;
    /**
     * 操作结果：操作失败，必要属性为空
     */
    int FAILURE_REQUIRED_NULL = 8;
    /**
     * 操作结果：操作失败，非法操作
     */
    int FAILURE_TYPE_NULL = 99;

    String[] DESCS = new String[]{
            "操作成功",
            "操作失败,实体（对象）没有ApiModel注解",
            "操作失败，ID冲突",
            "操作失败，对象所有属性为空",
            "操作失败，ID属性为空",
            "操作失败，内部错误",
            "操作失败，不允许传入集合",
            "操作失败，找不到没有数据",
            "操作失败，必填属性为空",
            "操作失败，非法操作"
    };

    /**
     * 获取操作结果的说明
     *
     * @param type 操作结果码
     * @return 操作结果说明
     * @Author 陈文
     * @Date 2020/4/13  20:42
     * @Description 可以通过重写该方法，来自定义返回值
     */

    default String getDesc(int type) {
        if (type >= DESCS.length) {
            return DESCS[DESCS.length - 1];
        }
        return DESCS[type];
    }

    static void main(String[] args) {
        BaseServiceType ss = new BaseServiceType() {

        };
        String desc = ss.getDesc(FAILURE_NO_LIST);
        System.out.println(desc);
    }

}
