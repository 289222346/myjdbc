package com.myjdbc.web.api.controller;

import com.alibaba.fastjson.JSONObject;
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
@RequestMapping(value = "/api/public")
public class PublicInfoController extends BaseController {


    @RequestMapping("/hello")
    public JSONObject helloHtml() {
        return createError("可能是错误吧");
    }

    @RequestMapping("/hello2")
    public ModelAndView helloHtml2() {
        return new ModelAndView("/hello");
    }
}