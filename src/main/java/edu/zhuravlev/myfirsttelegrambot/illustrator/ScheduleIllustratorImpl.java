package edu.zhuravlev.myfirsttelegrambot.illustrator;

import busentity.Bus;
import org.springframework.stereotype.Component;
import static edu.zhuravlev.myfirsttelegrambot.illustrator.HtmlStyleTelegramFormatter.format;
import static edu.zhuravlev.myfirsttelegrambot.illustrator.HtmlStyleTelegramFormatter.Styles.*;

import java.util.List;

@Component
public class ScheduleIllustratorImpl implements ScheduleIllustrator{



    @Override
    public String illustrateAll(List<Bus> buses) {
        StringBuilder builder = new StringBuilder(buses.size() * 2);
        String line = "\n-------------------------\n";

        for (int i = 0; i<buses.size(); i++) {
            builder.append(formatOneBus(buses.get(i)));
            if(i!=buses.size()-1) builder.append(line);
        }

        return builder.toString();
    }

    private String formatOneBus(Bus bus) {
        String busName = "Автобус " + format(bus.getBusName(), BOLD) + ":\n";
        String where;

        if(bus.isBusOnLive())
            where = "На карте - " + format(format(bus.getArrivedTime() + "\n", BOLD), UNDERLINE);
        else
            where = "По расписанию - " + format(bus.getArrivedTime() + "\n", ITALIC);

        String schedule = "Расписание: " + bus.getScheduleTime();

        return busName + where + schedule;
    }
}
