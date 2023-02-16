package edu.zhuravlev.busanalyzerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication()
public class MyFirstTelegramBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyFirstTelegramBotApplication.class, args);
    }

}
