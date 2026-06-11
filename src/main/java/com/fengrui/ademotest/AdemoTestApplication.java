package com.fengrui.ademotest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fengrui.ademotest.mapper")
public class AdemoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdemoTestApplication.class, args);
    }

}
