package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UsersRepository repository;
    UserController(UsersRepository repo) {
        this.repository=repo;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return repository.findAll();
    }
    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable long id) {
        return repository.findById(id);
    }
    @PostMapping("/users")
    public User postUser(@RequestBody HashMap<String,String> userMap) {
        if (userMap.containsKey("email") && userMap.containsKey("password") && !userMap.get("email").isEmpty() && !userMap.get("password").isEmpty()) {
            //TODO: EMAIL VALIDATION
            User user = new User(userMap.get("email"),userMap.get("password"));
            repository.save(user);
            return user;
        }
        return null;
    }
    @PatchMapping("/users/{id}")
    public Optional<User> patchUser(@PathVariable long id, @RequestBody HashMap<String,String> userMap) {
        Optional<User> user = repository.findById(id);
        user.ifPresent(u->{
            if (userMap.containsKey("email") && !userMap.get("email").isEmpty() ) {
                u.setEmail(userMap.get("email"));
            }
            if (userMap.containsKey("password") && !userMap.get("password").isEmpty() ) {
                u.setPassword(userMap.get("password"));
            }
            repository.save(u);
        });
        return user;
    }
}
