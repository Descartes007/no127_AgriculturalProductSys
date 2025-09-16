package com.agricultural.em;

import com.agricultural.em.utils.PathUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.agricultural.em.mapper")
@SpringBootApplication
public class AgriculturalProductApplication {

    public static void main(String[] args) {
        System.out.println("Project Path: " + PathUtils.getClassLoadRootPath());
        SpringApplication.run(AgriculturalProductApplication.class, args);
    }

}
