package edu.zhuravlev.busanalyzerbot.illustrator;

import busentity.Bus;

import java.util.List;

public interface ScheduleIllustrator {
    String illustrateAll(List<Bus> buses);
    List<Bus> orderBuses(List<Bus> buses);
    default String illustrateAllWithOrder(List<Bus> buses) {
        return illustrateAll(orderBuses(buses));
    }
}
