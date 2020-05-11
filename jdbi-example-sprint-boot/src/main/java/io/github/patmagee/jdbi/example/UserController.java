package io.github.patmagee.jdbi.example;

import org.jdbi.v3.core.Jdbi;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private Jdbi jdbi;

    public UserController(Jdbi jdbi){
        this.jdbi = jdbi;
        jdbi.useExtension(UserDao.class,UserDao::createUserTable);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user){
        user.setId(System.currentTimeMillis());
        jdbi.useExtension(UserDao.class, dao -> {
            dao.createUser(user);
        });
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers(){
        return jdbi.withExtension(UserDao.class, UserDao::getUsers);
    }

    @GetMapping("/users/{id}")
    public User getUsers(@PathVariable  Long id){
        return jdbi.withExtension(UserDao.class, dao -> dao.getUser(id));
    }

}
