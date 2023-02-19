package edu.zhuravlev.busanalyzerbot.repositories.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserTable, Long> {
    UserTable getUserTableByChatId(long chatId);
}
