package edu.zhuravlev.busanalyzerbot;

import busparser.BusParser;
import busparser.DefaultBusParser;
import edu.zhuravlev.busanalyzerbot.botcommands.MyCommands;
import edu.zhuravlev.busanalyzerbot.cashed.cash.CashedBusParser;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.Arrays;
import java.util.List;

@Configuration
@Data
@PropertySource("bot_config.properties")
public class BotConfig {
    @Value("${bot.name}")private String botName;
    @Value("${bot.token}")private String token;
    @Value("${busstop.request}") private String request;
    @Value("${busstop.filepath}") private String path;
    @Value("${bot.debug}") private boolean debugMode;
    @Value("${bot.helloMessage}") private String helloMessage;

    @Bean
    public BusParser busParser() {
        return new CashedBusParser(new DefaultBusParser());
    }

    @Bean
    public List<BotCommand> botCommands() {
        return Arrays.stream(MyCommands.values())
                .filter(c -> !c.equals(MyCommands.START))
                .map(MyCommands::getBotCommand)
                .toList();
    }
}
