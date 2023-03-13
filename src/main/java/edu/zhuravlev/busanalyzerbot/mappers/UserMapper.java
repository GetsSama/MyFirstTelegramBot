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

    @Mapping(target = "busStops", expression = "java(busStopsMappingByEquals(userTable, user))")
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

    default Set<BusStopTable> busStopsMappingByEquals(UserTable userTable, User user) {
        if(user.getBusStops().isEmpty())
            userTable.getBusStops().clear();
        else {
    //Need some optimization. Now naive realization.
            var busStopTableIter = userTable.getBusStops().iterator();
            while (busStopTableIter.hasNext()) {
                var busStopTable = busStopTableIter.next();
                var busStopEntity = BusStopMapper.INSTANCE.toEntity(busStopTable);
                busStopEntity.setUser(user);
                if(!user.getBusStops().contains(busStopEntity))
                    busStopTableIter.remove();
            }
            for (var busStop : user.getBusStops()) {
                var busStopTable = BusStopMapper.INSTANCE.toTable(busStop);
                busStopTable.setUser(userTable);
                userTable.getBusStops().add(busStopTable);
            }
        }

        return userTable.getBusStops();
    }
}
