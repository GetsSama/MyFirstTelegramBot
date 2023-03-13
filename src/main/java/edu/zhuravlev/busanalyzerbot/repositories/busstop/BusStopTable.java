package edu.zhuravlev.busanalyzerbot.repositories.busstop;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Parent;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BusStopTable {
    //@ToString.Exclude
    //@EqualsAndHashCode.Exclude
    @Parent
    private UserTable user;

    @Column(nullable = false)
    private String busStopUrl;

    @Column(nullable = false)
    private String busStopName;

    @Column(nullable = false)
    private String priorityBuses;
}
