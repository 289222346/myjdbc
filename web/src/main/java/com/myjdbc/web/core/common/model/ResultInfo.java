package com.myjdbc.web.core.common.model;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.core.util.StringUtil;
import lombok.Data;

/**
 * 返回结果
 *
 * @Author 陈文
 * @Date 2020/05/27  23:02
 */
@Data
public class ResultInfo extends JSONObject {

    /**
     * 结果状态
     */
    private int code;

    /**
     * 结果信息
     */
    private String resultMsg;

    /**
     * 附带信息
     */
    private Object data;

    public void setCode(Integer code) {
        if (code != null) {
            this.put("code", code);
        }
        this.code = code;
    }

    public void setResultMsg(String resultMsg) {
        if (StringUtil.isNotEmpty(resultMsg)) {
            this.put("resultMsg", resultMsg);
        }
        this.resultMsg = resultMsg;
    }

    public void setData(Object data) {
        if (data != null) {
            this.put("data", data);
        }
        this.data = data;
    }
}
