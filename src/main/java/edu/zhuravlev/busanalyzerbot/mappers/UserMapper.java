package edu.zhuravlev.busanalyzerbot.mappers;

import edu.zhuravlev.busanalyzerbot.entities.User;
import edu.zhuravlev.busanalyzerbot.repositories.user.UserTable;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BusStopMapper.class})
public interface UserMapper {
    User toEntity(UserTable userTable);
    UserTable toTable(User user);
}
