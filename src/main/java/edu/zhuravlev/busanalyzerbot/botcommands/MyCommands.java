package edu.zhuravlev.busanalyzerbot.botcommands;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

public enum MyCommands {
    START(new BotCommand("/start", "Стартовая команда, для новых пользователей")),
    ADDBUSSTOP(new BotCommand("/add_bus_stop", "Добавить новую автобусную остановку")),
    HELP(new BotCommand("/help", "Полный список команд с описанием")),
    EDITBUSSTOP(new BotCommand("/edit_bus_stop", "Редактировать достуные остановки")),
    DELETEBUSSTOP(new BotCommand("/delete", "Удалить остановку"));
    private final BotCommand command;
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
