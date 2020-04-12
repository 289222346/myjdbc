package com.myjdbc.core.service;

/**
 * 操作结果状态码
 *
 * @Author 陈文
 * @Date 2020/4/11  17:32
 */
public interface BaseServiceType {

    /**
     * 操作结果：保存成功
     */
    int SAVE_OK = 0;
    /**
     * 操作结果：保存失败，没有ApiModel注解
     */
    int SAVE_NO_LACK_MODEL = 1;
    /**
     * 操作结果：保存失败，ID冲突
     */
    int SAVE_NO_ID_CLASH = 2;
    /**
     * 操作结果：保存失败，对象所有属性为空
     */
    int SAVE_NO_ALL_NULL = 3;
    /**
     * 操作结果：保存失败，IP属性为空
     */
    int SAVE_NO_IP_NULL = 4;
    /**
     * 操作结果：保存失败，内部错误
     */
    int SAVE_NO_INSIDE_ERROR = 5;
    /**
     * 操作结果：保存失败，非法操作
     */
    int SAVE_TYPE_NULL = 99;

    String[] DESCS = new String[]{
            "保存成功",
            "保存失败,实体（对象）没有ApiModel注解",
            "保存失败，ID冲突",
            "保存失败，对象所有属性为空",
            "保存失败，IP属性为空",
            "保存失败，非法操作"
    };

    /**
     * 获取操作结果的说明
     *
     * @param type 操作结果码
     * @return
     */
    static String getDesc(int type) {
        if (type >= DESCS.length) {
            return DESCS[DESCS.length - 1];
        }
        return DESCS[type];
    }

    static void main(String[] args) {
        String desc = getDesc(SAVE_TYPE_NULL);
        System.out.println(desc);
    }

}
