package edu.zhuravlev.busanalyzerbot.router;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateRouter {
    void process(Update update);
}
