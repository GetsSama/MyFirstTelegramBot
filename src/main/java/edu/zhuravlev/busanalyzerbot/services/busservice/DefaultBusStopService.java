package edu.zhuravlev.busanalyzerbot.services.busservice;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.mappers.BusStopMapper;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopRepository;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultBusStopService implements BusStopService{
    private final UserRepository userRepository;
    private final BusStopRepository busStopRepository;
    private final BusStopMapper busStopMapper;

    @Override
    public void deleteBusStop(BusStop busStop) {
        busStopRepository.delete(getBusStopTable(busStop));
    }

    @Override
    public void updateBusStop(BusStop busStop) {
        var busStopTable = getBusStopTable(busStop);
        busStopMapper.updateBusStopTable(busStopTable, busStop);
        busStopRepository.save(busStopTable);
    }

    private BusStopTable getBusStopTable(BusStop busStop) {
        var userChatId = busStop.getUser().getChatId();
        var userTable = userRepository.getUserTableByChatId(userChatId);
        return userTable.getBusStops()
                .stream()
                .filter(b -> b.getBusStopName().equals(busStop.getBusStopName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("For user with id:" + userChatId + " no such bus stop with name " + busStop.getBusStopName()));
    }
}
