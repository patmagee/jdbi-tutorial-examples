package io.github.patmagee.jdbi.example;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface UserDao {

    @Transaction
    @SqlUpdate("CREATE TABLE IF NOT EXISTS users(id BIGINT NOT NULL PRIMARY KEY, first_name VARCHAR(48), last_name VARCHAR(48), phone_number VARCHAR(48))")
    void createUserTable();

    @Transaction
    @SqlUpdate("INSERT INTO users(id,first_name,last_name,phone_number) VALUES(:id,:firstName,:lastName,:phoneNumber)")
    void createUser(@BindBean User user);

    @SqlQuery("SELECT * FROM users")
    @RegisterBeanMapper(User.class)
    List<User> getUsers();

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    @RegisterBeanMapper(User.class)
    User getUser(@Bind("id") Long id);

}