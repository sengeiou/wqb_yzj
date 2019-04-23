package com.wqb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {
        "classpath:config/address.properties",
        "classpath:config/filePath.properties"})
public class BookkeeperApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeeperApplication.class, args);
    }
}
