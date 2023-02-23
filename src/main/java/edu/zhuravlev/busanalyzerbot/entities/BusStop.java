package edu.zhuravlev.busanalyzerbot.entities;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusStop {
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;
    private String busStopUrl;
    private Set<String> priorityBuses;
}
