package edu.zhuravlev.busanalyzerbot.repositories.user;

import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import jakarta.persistence.*;
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
@Entity
@Table(name="users")
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@EqualsAndHashCode.Exclude
    private Long id;

    private String name;

    @Column(nullable = false)
    private String chatId;

    @EqualsAndHashCode.Exclude
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "busStops")
    private Set<BusStopTable> busStops = new HashSet<>();
}
