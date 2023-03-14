package edu.zhuravlev.busanalyzerbot.repositories.user;

import busentity.Bus;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

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
//    @SQLDelete(sql = "delete from bus_stops where user_table_id = ? and bus_stop_name = ?" +
//                                                " and bus_stop_url = ? and priority_buses = ?")
    private Set<BusStopTable> busStops = new HashSet<>();

    public void addBusStopTable(BusStopTable busStopTable) {
        busStopTable.setUser(this);
        busStops.add(busStopTable);
    }
}
