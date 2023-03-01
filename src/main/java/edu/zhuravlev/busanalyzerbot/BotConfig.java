package edu.zhuravlev.busanalyzerbot;

import busparser.BusParser;
import busparser.DefaultBusParser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("bot_config.properties")
public class BotConfig {
    @Value("${bot.name}")private String botName;
    @Value("${bot.token}")private String token;
    @Value("${busstop.request}") private String request;
    @Value("${busstop.filepath}") private String path;
    @Value("${bot.debug}") private boolean debugMode;

    @Bean
    public BusParser getBusParser() {
        return new DefaultBusParser();
    }
}
