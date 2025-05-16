package org.example.univer;

import org.example.univer.config.AppSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppSettings.class)
public class UniverApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniverApplication.class, args);
    }
}