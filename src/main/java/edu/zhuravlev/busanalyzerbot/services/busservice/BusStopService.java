package edu.zhuravlev.busanalyzerbot.services.busservice;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;

public interface BusStopService {
    void deleteBusStop(BusStop busStop);
    void updateBusStop(BusStop busStop);
}
