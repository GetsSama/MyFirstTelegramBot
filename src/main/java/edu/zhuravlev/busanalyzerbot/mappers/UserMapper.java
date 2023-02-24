package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.BusStop;
import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {BusStopMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toEntity(UserTable userTable);
    UserTable toTable(User user);

    @Mapping(target = "busStops", expression = "java(busStopsMappingByEquals(userTable.getBusStops(), user.getBusStops()))")
    @Mapping(target = "chatId", ignore = true)
    void updateUserTable(@MappingTarget UserTable userTable, User user);

    @AfterMapping
    default void setToBusStopRelations(@MappingTarget User user) {
        var busStops = user.getBusStops();
        for(var busStop : busStops)
            busStop.setUser(user);
    }

    @AfterMapping
    default void setToBusStopTableRelations(@MappingTarget UserTable userTable) {
        var busStopsTables = userTable.getBusStops();
        for(var busStopTable : busStopsTables)
            busStopTable.setUser(userTable);
    }

    default Set<BusStopTable> busStopsMappingByEquals(Set<BusStopTable> busStopTables, Set<BusStop> busStops) {
        if(busStops == null)
            busStopTables.clear();
        else
            for(var busStop : busStops) {
                BusStopTable mappedBus = BusStopMapper.INSTANCE.toTable(busStop);
                busStopTables.add(mappedBus);
            }
        return busStopTables;
    }
}
