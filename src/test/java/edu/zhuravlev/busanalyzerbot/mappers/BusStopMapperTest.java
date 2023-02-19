package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusStopMapperTest {

    @Test
    void getListPriorityBusesFromString() {
        String testStringBuses = "404, 642,m90,   273";
        List<String> result = List.of("404", "642", "m90", "273");
        BusStopMapper busStopMapper = new BusStopMapper() {
            @Override
            public BusStop toEntity(BusStopTable busStopTable) {
                return null;
            }

            @Override
            public BusStopTable toTable(BusStop busStop) {
                return null;
            }
        };

        List<String> tested = busStopMapper.getListPriorityBusesFromString(testStringBuses);
        assertEquals(result, tested);
    }

    @Test
    void getStringPriorityBusesFromList() {
        var testListBuses = List.of("404", "642", "m90", "273");
        String result = "404,642,m90,273";
        BusStopMapper busStopMapper = new BusStopMapper() {
            @Override
            public BusStop toEntity(BusStopTable busStopTable) {
                return null;
            }

            @Override
            public BusStopTable toTable(BusStop busStop) {
                return null;
            }
        };

        assertEquals(result, busStopMapper.getStringPriorityBusesFromList(testListBuses));
    }
}