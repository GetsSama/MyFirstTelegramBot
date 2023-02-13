package edu.zhuravlev.busanalyzerbot.illustrator;

import busentity.Bus;

import java.util.List;

public interface ScheduleIllustrator {
    String illustrateAll(List<Bus> buses);
}
