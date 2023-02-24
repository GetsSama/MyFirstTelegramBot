package edu.zhuravlev.busanalyzerbot.repositories.busstop;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bus_stops")
public class BusStopTable {
    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private UserTable user;
    private String busStopUrl;
    private String busStopName;
    private String priorityBuses;
}
