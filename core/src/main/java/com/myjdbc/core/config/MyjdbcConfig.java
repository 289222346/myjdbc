package com.myjdbc.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;



@ComponentScan("com.myjdbc")
@Configuration
public class MyjdbcConfig {

    // 通过@Bean注解来表明是一个Bean对象，相当于xml中的<bean>

    /**
     *
     * @return
     */
//    @Bean
//    public MyDaoBeanScannerConfigurer getMyDaoBeanScannerConfigurer() {
//        return new MyDaoBeanScannerConfigurer(); // 直接new对象做演示
//    }
}
