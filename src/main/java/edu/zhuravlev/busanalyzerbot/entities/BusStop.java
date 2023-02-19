package edu.zhuravlev.busanalyzerbot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    private String busStopUrl;
    private List<String> priorityBuses;
}
