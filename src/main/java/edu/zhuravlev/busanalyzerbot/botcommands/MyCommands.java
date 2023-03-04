package edu.zhuravlev.busanalyzerbot.botcommands;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public enum MyCommands {
    START(new BotCommand("/start", "Start bot command, for new users.")),
    ADDBUSSTOP(new BotCommand("/add_bus_stop", "Add new bus stop.")),
    HELP(new BotCommand("/help", "Get all available commands with description."));

    private BotCommand command;
    MyCommands(BotCommand command){
        this.command = command;
    }

    public BotCommand getBotCommand() {
        return this.command;
    }

    @Override
    public String toString() {
        return command.getCommand();
    }
}
