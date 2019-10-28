package com.wqlm.boot.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wqlm.boot.user.dao")
@SpringBootApplication
public class BootUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootUserApplication.class, args);
    }

}
