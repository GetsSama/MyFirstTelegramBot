package edu.zhuravlev.busanalyzerbot.cashed.cash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashedBusParserTest {

    @Test
    void getBusIdTest() {
        var instance = new CashedBusParser(null);
        var url = "https://yandex.ru/maps/213/moscow/stops/stop__9643979/?ll=37.546412%2C55.647082&tab=overview&z=17.23";
        var busId = "stop__9643979";

//        System.out.println(instance.getBusId(url));
//        assertEquals(busId, instance.getBusId(url));
    }
}