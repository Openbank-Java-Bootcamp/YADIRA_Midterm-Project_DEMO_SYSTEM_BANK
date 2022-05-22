package com.example.systembank.controller.impl;

import com.example.systembank.model.users.Role;
import com.example.systembank.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class RoleControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    }


   /* @Test
    void saveRole_Unprocessable() throws Exception {
        Role newRole = new Role(null, "ACCOUNT_HOLDER");
        String body = objectMapper.writeValueAsString(newRole);
        mockMvc.perform(post("/api/roles")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnprocessableEntity());
    }*/

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void saveRole_validate() throws Exception {
        Role newRole = new Role(null, "ANALYST");
        String body = objectMapper.writeValueAsString(newRole);
        mockMvc.perform(post("/api/roles")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

    }
}