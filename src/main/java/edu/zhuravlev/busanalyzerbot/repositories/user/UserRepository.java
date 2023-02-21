package edu.zhuravlev.busanalyzerbot.repositories.user;

import edu.zhuravlev.busanalyzerbot.repositories.busstop.BusStopTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserTable, Long> {
    UserTable getUserTableByChatId(long chatId);
    @Transactional
    @Modifying
    @Query("UPDATE UserTable as u SET u.busStops = ?1 WHERE u.chatId = ?2")
    void updateUserTableByChatId(List<BusStopTable> busStopTables, long chatId);
}
