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
    @DeleteMapping("/users/{id}")
    public HashMap<String,Long> deleteUser(@PathVariable long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent())
            repository.deleteById(id);
        HashMap<String,Long> ret = new HashMap<>();
        ret.put("count",repository.count());
        return ret;
    }
    @PostMapping("/users/authenticate")
    public HashMap<String,Object> authenticate(@RequestBody HashMap<String,String> user) {
        Boolean authenticated = false;
        User foundUser=null;
        HashMap<String, Object> ret = new HashMap<>();
        if (user.containsKey("email") && user.containsKey("password")) {
            foundUser = repository.findByEmail(user.get("email"));

            if (foundUser != null)
                authenticated = foundUser.getPassword().equals(user.get("password"));
        }
            ret.put("authenticated", authenticated);
            if (authenticated) {
                ret.put("user", foundUser);
            }

        return ret;
    }
}
