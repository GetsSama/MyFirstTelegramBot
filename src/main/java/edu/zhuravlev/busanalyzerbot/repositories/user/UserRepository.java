package edu.zhuravlev.busanalyzerbot.repositories.user;

import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends CrudRepository<UserTable, Long> {
    UserTable getUserTableByChatId(String chatId);
    boolean existsByChatId(String chatId);
//    @Query("delete from UserTable as ut where ut.busStops.busStopName = :busName")
    void deleteBusStopTable(BusStopTable busStopTable);
}
