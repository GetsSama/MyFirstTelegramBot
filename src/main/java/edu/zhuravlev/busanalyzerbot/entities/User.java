package edu.zhuravlev.busanalyzerbot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private long chatId;
    private Set<BusStop> busStops;
}
