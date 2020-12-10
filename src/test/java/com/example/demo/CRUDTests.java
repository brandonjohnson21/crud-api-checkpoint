package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CRUDTests {
    @Autowired
    MockMvc mvc;
    @Autowired
    UsersRepository repository;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        repository.saveAll(Arrays.asList(new User("james@jamestown.us","123"),new User("Holly@christmas.np","hohoho")));

    }

    @Test
    @Rollback
    @Transactional
    public void GetAllTest() throws Exception {
        mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email",equalTo("james@jamestown.us")))
                .andExpect(jsonPath("$[0]",not(hasProperty("password"))))
        ;
    }

    @Test
    @Rollback
    @Transactional
    public void GetOneTest() throws Exception {
        long id = repository.findAll().iterator().next().getId();
        Iterator<User> it = repository.findAll().iterator();
        assertTrue(it.hasNext());
        do {
            if (!it.hasNext())
                break;
            User u = it.next();
            if (u.getEmail().equals("james@jamestown.us")){
                id = u.getId();
                break;
            }
        } while (it.hasNext());
        mvc.perform(get("/users/"+id).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",equalTo("james@jamestown.us")))
                .andExpect(jsonPath("$",not(hasProperty("password"))))
        ;
    }
    @Test
    @Rollback
    @Transactional
    public void PostOneTest() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"john@example.com\",\"password\": \"something-secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",equalTo("john@example.com")))
                .andExpect(jsonPath("$",not(hasProperty("password"))))
        ;


    }
    @Test
    @Rollback
    @Transactional
    public void PatchOneTest() throws Exception {
    }
    @Test
    @Rollback
    @Transactional
    public void PatchOneTest2() throws Exception {
    }
    @Test
    @Rollback
    @Transactional
    public void DeleteOneTest() throws Exception {
    }
    @Test
    @Rollback
    @Transactional
    public void GoodAuthenticateTest() throws Exception {
    }
    @Test
    @Rollback
    @Transactional
    public void BadAuthenticateTest() throws Exception {
    }

}
