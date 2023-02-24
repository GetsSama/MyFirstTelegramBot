package edu.zhuravlev.busanalyzerbot.repositories.busstop;

import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusStopTableTest {

    @Test
    void testEquals() {
        UserTable user = new UserTable();

        BusStopTable bus1 = new BusStopTable(1L, user, "someUrl1", "404,642");
        BusStopTable bus2 = new BusStopTable(1L, new UserTable(), "someUrl1", "404,642");
        BusStopTable bus3 = new BusStopTable(2L, null, "someUrl2", "273");

        assertAll(()->assertEquals(bus1, bus2),
                ()->assertNotEquals(bus1, bus3),
                ()->assertNotEquals(bus2, bus3));
    }

    @Test
    void testEqualsAndHashCode() {

    }
}