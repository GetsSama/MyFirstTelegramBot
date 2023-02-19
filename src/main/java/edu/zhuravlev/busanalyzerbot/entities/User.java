package edu.zhuravlev.busanalyzerbot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private long chatId;
    private List<BusStop> busStops;
}
