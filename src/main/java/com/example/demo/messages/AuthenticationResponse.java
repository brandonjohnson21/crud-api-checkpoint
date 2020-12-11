package com.example.demo.messages;

import com.example.demo.User;
import com.example.demo.Views;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonView;

public class AuthenticationResponse {
    @JsonView(Views.Simple.class)
    private boolean authenticated;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonView(Views.Simple.class)

    private User user;
    @JsonSetter("authenticated")
    public void authenticate(boolean auth) {
        this.authenticated=auth;
    }
    public AuthenticationResponse authenticate() {
        this.authenticate(true);
        return this;
    }
    @JsonSetter("user")
    public AuthenticationResponse user(User u) {
        this.user = u;
        return this;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public User getUser() {
        return user;
    }
}