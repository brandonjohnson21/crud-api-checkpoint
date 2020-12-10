package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface UsersRepository  extends CrudRepository<User, Long> {

}
