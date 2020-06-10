package com.myjdbc.web.api.controller;

import com.myjdbc.web.core.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 公共信息开放接口
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@Controller()
@RequestMapping(value = "/api/public")
public class WebController extends BaseController {


    @RequestMapping("/hello2")
    public String helloHtml2() {
        System.out.println("收到请求2");
        return "/hello";
    }
}