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

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    Long id0;

    @BeforeEach
    public void init() {

        repository.saveAll(Arrays.asList(new User("james@jamestown.us","123"),new User("Holly@christmas.np","hohoho")));
        Iterator<User> it = repository.findAll().iterator();
        do {
            if (!it.hasNext())
                break;
            User u = it.next();
            if (u.getEmail().equals("james@jamestown.us")){
                id0 = u.getId();
                break;
            }
        } while (it.hasNext());
    }

    @Test
    @Rollback
    @Transactional
    public void GetAllTest() throws Exception {
        mvc.perform(get("/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email",equalTo("james@jamestown.us")))
                .andExpect(jsonPath("$[0]",not(hasKey("password"))))
        ;
    }

    @Test
    @Rollback
    @Transactional
    public void GetOneTest() throws Exception {
        mvc.perform(get("/users/"+id0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",equalTo("james@jamestown.us")))
                .andExpect(jsonPath("$",not(hasKey("password"))))
        ;
    }
    @Test
    @Rollback
    @Transactional
    public void PostOneTest() throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"john@example.com\",\"password\": \"something-secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",not(hasKey("password"))))
                .andDo((handler)->assertNotNull(repository.findByEmail("john@example.com")))
        ;


    }
    @Test
    @Rollback
    @Transactional
    public void PatchOneTest() throws Exception {
        mvc.perform(patch("/users/"+id0).contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"johnny@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",equalTo("johnny@example.com")))
                .andExpect(jsonPath("$",not(hasKey("password"))))
                .andDo((handler)->assertNotNull(repository.findByEmail("johnny@example.com")))
        ;
    }
    @Test
    @Rollback
    @Transactional
    public void PatchOneTest2() throws Exception {
        mvc.perform(patch("/users/"+id0).contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"jornny@example.com\",\"password\":\"fantastic\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email",equalTo("jornny@example.com")))
                .andExpect(jsonPath("$",not(hasKey("password"))))
        ;
    }
    @Test
    @Rollback
    @Transactional
    public void DeleteOneTest() throws Exception {
        mvc.perform(delete("/users/"+id0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count",equalTo(1)))
        ;
    }
    @Test
    @Rollback
    @Transactional
    public void GoodAuthenticateTest() throws Exception {
        mvc.perform(post("/users/authenticate").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"james@jamestown.us\",\"password\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated",equalTo(true)))
                .andExpect(jsonPath("$", hasKey("user")))
                .andExpect(jsonPath("$.user",hasKey("id")))
                .andExpect(jsonPath("$.user.email",equalTo("james@jamestown.us")))
                .andExpect(jsonPath("$.user",not(hasKey("password"))))

        ;
    }
    @Test
    @Rollback
    @Transactional
    public void BadAuthenticateTest() throws Exception {
        mvc.perform(post("/users/authenticate").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"james@jamestown.us\",\"password\":\"abc\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated",equalTo(false)))
                .andExpect(jsonPath("$",not(hasProperty("user"))))
        ;
    }

}
