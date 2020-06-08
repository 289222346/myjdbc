package com.myjdbc.web.core.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.core.service.BaseServiceType;
import com.myjdbc.web.core.common.model.ResultInfo;

/**
 * 所有控制层的基类
 * 用于返回统一的返回值
 *
 * @author 陈文
 * @date 2020/05/27  23:02
 */
public abstract class BaseController implements BaseServiceType {

    /**
     * 调用接口失败返回
     *
     * @param msg
     * @return
     */
    protected JSONObject createError(String msg) {
        return createResultInfo(FAILURE_TYPE_NULL, msg, null);
    }

    /**
     * 传入BaseServiceType的对应值
     *
     * @param code {@link BaseServiceType#getDesc(int)}
     * @return
     */
    protected JSONObject createResultInfo(int code) {
        return createResultInfo(code, null);
    }

    /**
     * 传入BaseServiceType的对应值
     *
     * @param code {@link BaseServiceType#getDesc(int)}
     * @param data 需要传输到客户端（浏览器）的数据
     * @return
     */
    protected JSONObject createResultInfo(int code, Object data) {
        return createResultInfo(code, getDesc(code), data);
    }

    /**
     * 传入BaseServiceType的对应值
     *
     * @param code {@link BaseServiceType#getDesc(int)}
     * @param msg  提示给用户的信息
     * @param data 需要传输到客户端（浏览器）的数据
     * @return
     */
    protected JSONObject createResultInfo(int code, String msg, Object data) {
        return toResultInfo(code, msg, data);
    }


    /**
     * 转换成JSONMessage格式输出
     *
     * @param code
     * @param msg
     * @return
     */
    private JSONObject toResultInfo(int code, String msg, Object data) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setResultMsg(msg);
        if (BaseServiceType.SUCCESS != code) {
            code = -1;
        }
        resultInfo.setCode(code);
        resultInfo.setData(data);
        return resultInfo;
    }

}
