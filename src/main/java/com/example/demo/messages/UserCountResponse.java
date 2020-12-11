package com.example.demo.messages;

import org.springframework.data.repository.CrudRepository;

public class UserCountResponse {
    long count;
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public UserCountResponse(CrudRepository<?,?> repo) {
        this.count = repo.count();
    }
}
