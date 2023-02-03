package edu.zhuravlev.myfirsttelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyFirstTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFirstTelegramBotApplication.class, args);
    }

}
