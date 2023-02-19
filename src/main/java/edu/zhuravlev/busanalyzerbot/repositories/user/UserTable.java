package edu.zhuravlev.busanalyzerbot.repositories.user;

import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private long chatId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BusStopTable> busStops;
}
