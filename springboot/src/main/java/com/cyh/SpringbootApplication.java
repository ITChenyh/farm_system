package com.cyh;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@MapperScan("com.cyh.mapper")
public class SpringbootApplication {

    public static void main(String[] args) throws InterruptedException {

        SpringApplication.run(SpringbootApplication.class, args);

    }

}
