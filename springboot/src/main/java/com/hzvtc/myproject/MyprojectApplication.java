package com.hzvtc.myproject;

import com.hzvtc.myproject.config.Constant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.hzvtc.myproject.dao")
@EnableConfigurationProperties({Constant.class})
@EnableScheduling
public class MyprojectApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext application
                = SpringApplication.run(MyprojectApplication.class, args);
        Environment env = application.getEnvironment();
        String port = env.getProperty("server.port");
        System.out.println("Local:http://localhost:" + port);
    }

}
