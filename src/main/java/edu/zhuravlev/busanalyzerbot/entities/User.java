package edu.zhuravlev.busanalyzerbot.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;
    private String chatId;
    @EqualsAndHashCode.Exclude
    private Set<BusStop> busStops = new HashSet<>();

    public User(String name, String chatId) {
        this.name = name;
        this.chatId = chatId;
    }

    public void addBusStop(BusStop busStop) {
        busStop.setUser(this);
        busStops.add(busStop);
    }
}
