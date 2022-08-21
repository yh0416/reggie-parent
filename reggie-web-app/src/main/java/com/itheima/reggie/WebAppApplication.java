package com.itheima.reggie;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@MapperScan("com.itheima.reggie.mapper")
public class WebAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAppApplication.class,args);
        log.info("启动成功");
    }
}
