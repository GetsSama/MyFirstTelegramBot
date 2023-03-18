package edu.zhuravlev.busanalyzerbot.illustrator;

import busentity.Bus;
import com.google.common.collect.Streams;
import org.apache.el.stream.Stream;
import org.springframework.stereotype.Component;
import static edu.zhuravlev.busanalyzerbot.illustrator.HtmlStyleTelegramFormatter.format;
import static edu.zhuravlev.busanalyzerbot.illustrator.HtmlStyleTelegramFormatter.Styles.*;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Component
public class ScheduleIllustratorImpl implements ScheduleIllustrator{
    private final Comparator<Bus> byTimeArrivedOnMap = (b1, b2) -> {
        int b1Minutes;
        int b2Minutes;
        var b1Split = b1.getArrivedTime().split(" ");
        var b2Split = b2.getArrivedTime().split(" ");

        if(b1Split.length == 2)
            b1Minutes = Integer.parseInt(b1Split[0]);
        else
            b1Minutes = Integer.parseInt(b1Split[1]);

        if(b2Split.length == 2)
            b2Minutes = Integer.parseInt(b2Split[0]);
        else
            b2Minutes = Integer.parseInt(b2Split[1]);

        return Integer.compare(b1Minutes, b2Minutes);
    };
    private final Comparator<Bus> byTimeArrivedSchedule = (b1, b2) -> {
        var b2Time = LocalTime.parse(b2.getArrivedTime());
        var b1Time = LocalTime.parse(b1.getArrivedTime());
        return b1Time.compareTo(b2Time);
    };
    private final Predicate<Bus> onMap = Bus::isBusOnLive;
    private final Predicate<Bus> arrivedNow = b -> b.getArrivedTime().equals("прибывает");
    private final Predicate<Bus> byLocalTimeFormat = b -> b.getArrivedTime().split(" ").length == 1;

    @Override
    public String illustrateAll(List<Bus> buses) {
        var orderedBusList = orderBuses(buses);
        StringBuilder builder = new StringBuilder(orderedBusList.size() * 2);
        String line = "\n-------------------------\n";

        for (int i = 0; i<orderedBusList.size(); i++) {
            builder.append(formatOneBus(orderedBusList.get(i)));
            if(i!=orderedBusList.size()-1) builder.append(line);
        }

        return builder.toString();
    }

    private String formatOneBus(Bus bus) {
        String busName = "Автобус " + format(bus.getBusName(), BOLD) + ":\n";
        String where;

        if(bus.isBusOnLive())
            where = "На карте - " + format(format(bus.getArrivedTime(), BOLD), UNDERLINE);
        else
            where = "По расписанию - " + format(bus.getArrivedTime(), ITALIC);

        String schedule;
        if(!bus.getScheduleTime().isEmpty()) {
            schedule = "\nРасписание: " + bus.getScheduleTime();
            return busName + where + schedule;
        }

        return busName + where;
    }

    private List<Bus> orderBuses(List<Bus> buses) {
        var arrived = buses.stream()
                .filter(onMap)
                .filter(arrivedNow);
        var sortedOnMap = buses.stream()
                .filter(onMap)
                .filter(arrivedNow.negate())
                .sorted(byTimeArrivedOnMap);
        var sortedOnScheduleFormat1 = buses.stream()
                .filter(onMap.negate())
                .filter(byLocalTimeFormat)
                .sorted(byTimeArrivedSchedule);
        var sortedOnScheduleFormat2 = buses.stream()
                .filter(onMap.negate())
                .filter(byLocalTimeFormat.negate())
                .sorted(byTimeArrivedOnMap);
        return Streams.concat(arrived, sortedOnMap, sortedOnScheduleFormat1, sortedOnScheduleFormat2).toList();
    }
}
