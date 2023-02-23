package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class, BusStopMapperImpl.class})
class UserMapperTest {
    @Autowired
    private UserMapper mapper;


    @Test
    void toEntity() {
        BusStopTable busStopTable = new BusStopTable(1L, null, "someUrl1", "404, 642");
        UserTable userTable = new UserTable(12L, "Nik", 123L, Set.of(busStopTable));
        busStopTable.setUser(userTable);

        BusStop busStop = new BusStop(null, "someUrl1", Set.of("404", "642"));
        User user = new User("Nik", 123L, Set.of(busStop));
        busStop.setUser(user);

        User result = mapper.toEntity(userTable);

        assertEquals(user, result, "Not equals!");
    }

    @Test
    void toTable() {
        BusStopTable busStopTable = new BusStopTable(null, null, "someUrl1", "642,404");
        UserTable userTable = new UserTable(null, "Nik", 123L, Set.of(busStopTable));
        busStopTable.setUser(userTable);

        BusStop busStop = new BusStop(null, "someUrl1", Set.of("404", "642"));
        User user = new User("Nik", 123L, Set.of(busStop));
        busStop.setUser(user);

        UserTable result = mapper.toTable(user);

        assertEquals(userTable, result);
    }
}