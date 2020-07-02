package com.myjdbc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.web.core.automation.util.JavaScriptGenerate;
import com.myjdbc.web.core.common.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 公共信息开放接口
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@RestController()
@RequestMapping(name = "publicInfo", path = {"/api/public", "/api"})
public class PublicInfoController extends BaseController {

    /**
     * 第一个注释
     *
     * @param name  姓名
     * @param value 值
     * @return
     */
    @RequestMapping(path = "/hello")
    public JSONObject helloHtml(String name, Integer value) {
        System.out.println("收到请求1");
        return createError("可能是错误吧");
    }


    /**
     * @return
     */
    @RequestMapping(path = "/ccc")
    public JSONObject ccc() {
        String s = JavaScriptGenerate.generateScriptStr(WebController.class);
        System.out.println(s);
        return createResultInfo(SUCCESS, s);
    }


}