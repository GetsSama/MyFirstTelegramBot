package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {BusStopMapper.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User toEntity(UserTable userTable);
    UserTable toTable(User user);

    void updateUserTable(@MappingTarget UserTable userTable, User user);

    @AfterMapping
    private void setToBusStopRelations(@MappingTarget User user) {
        var busStops = user.getBusStops();
        for(var busStop : busStops)
            busStop.setUser(user);
    }

    @AfterMapping
    private void setToBusStopTableRelations(@MappingTarget UserTable userTable) {
        var busStopsTables = userTable.getBusStops();
        for(var busStopTable : busStopsTables)
            busStopTable.setUser(userTable);
    }
}
