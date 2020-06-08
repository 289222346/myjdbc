package com.myjdbc.web.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.myjdbc.mymongodb.service.SystemMongoService;
import com.myjdbc.web.core.common.controller.BaseController;
import com.myjdbc.web.core.user.model.BaseUserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共信息开放接口
 *
 * @author 陈文
 * @date 2020/05/27  22:38
 */
@RestController()
@RequestMapping(value = "/console/user")
public class UserController extends BaseController {

    @Autowired
    private SystemMongoService systemMongoService;

    @RequestMapping("/add")
    public JSONObject add(BaseUserPO baseUserPO) {
        {
            int code = systemMongoService.save(baseUserPO);
            return createResultInfo(code);
        }
    }


}