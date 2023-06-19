package com.example.sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
public class OdeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdeApplication.class, args);
    }


}
