package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

}
