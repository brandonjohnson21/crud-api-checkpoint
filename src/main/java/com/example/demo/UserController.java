package com.example.demo;

import com.example.demo.messages.AuthenticationResponse;
import com.example.demo.messages.UserCountResponse;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
public class UserController {
    private final UsersRepository repository;
    UserController(UsersRepository repo) {
        this.repository=repo;
    }

    @JsonView(Views.Simple.class)
    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return repository.findAll();
    }
    @JsonView(Views.Simple.class)
    @GetMapping("/users/{id}")
    public Optional<User> getUser(@PathVariable long id) {
        return repository.findById(id);
    }
    @JsonView(Views.Simple.class)
    @PostMapping("/users")
    public User postUser(@RequestBody User userMap) {
        if (userMap.getEmail()!=null && userMap.getPassword()!=null && !userMap.getEmail().isEmpty() && !userMap.getPassword().isEmpty()) {
            //TODO: EMAIL VALIDATION
            User user = new User(userMap.getEmail(),userMap.getPassword());
            repository.save(user);
            return user;
        }
        return null;
    }
    @JsonView(Views.Simple.class)
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
    public UserCountResponse deleteUser(@PathVariable long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent())
            repository.deleteById(id);
        return new UserCountResponse(repository);
    }
    @JsonView(Views.Simple.class)
    @PostMapping("/users/authenticate")
    public AuthenticationResponse authenticate(@RequestBody User user) {
        User foundUser;
        AuthenticationResponse msg = new AuthenticationResponse();
        if (user.getEmail() != null && !user.getEmail().isEmpty() && user.getPassword()!=null) {
            foundUser = repository.findByEmail(user.getEmail());
            if (foundUser != null)
                if (foundUser.getPassword().equals(user.getPassword()))
                    msg.authenticate().user(foundUser);
        }
        return msg;
    }
}
