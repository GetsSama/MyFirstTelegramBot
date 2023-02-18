package edu.zhuravlev.busanalyzerbot.repositories.busstop;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus_stops")
public class BusStopTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private UserTable user;
    private String busStopUrl;
    private String priorityBuses;
}
