package edu.zhuravlev.busanalyzerbot.entities;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BusStopTest {

    @Test
    void testEquals() {
        var user = new User();
        user.setName("Nik");
        user.setChatId("123");

        var busStop1 = new BusStop();
        busStop1.setBusStopUrl("url1");
        busStop1.setBusStopName("bus1");
        busStop1.setPriorityBuses(Set.of("20", "30", "40"));

        user.addBusStop(busStop1);

        var busStop2 = new BusStop();
        busStop2.setBusStopUrl("url1");
        busStop2.setBusStopName("bus1");
        busStop2.setPriorityBuses(Set.of("20", "30", "40"));
        busStop2.setUser(user);

        assertEquals(busStop1, busStop2);
        assertEquals(busStop1, busStop1);
        assertEquals(busStop1.hashCode(), busStop1.hashCode());
        assertEquals(busStop1.hashCode(), busStop2.hashCode());
        assertTrue(busStop1.equals(busStop2));
        assertTrue(busStop1.hashCode()==busStop2.hashCode());
        System.out.println(user.getBusStops());
        assertTrue(user.getBusStops().contains(busStop1));

        user.addBusStop(busStop2);

        assertEquals(busStop1, busStop2);

    }
}