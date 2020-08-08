package com.myjdbc.web;

import com.myjdbc.web.socket.MonitoringCenter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 *
 * @author 陈文
 */
@ComponentScan({"com.myjdbc"})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        MonitoringCenter.startListening();
    }

}
