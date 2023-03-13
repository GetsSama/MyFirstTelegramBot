package edu.zhuravlev.busanalyzerbot.entities;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    @ToString.Exclude
    private User user;
    private String busStopUrl;
    private String busStopName;
    private Set<String> priorityBuses;
}
