package com.example.systembank.controller.impl;

import com.example.systembank.DTO.AccountHolderDTO;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.Address;
import com.example.systembank.model.users.Admin;
import com.example.systembank.model.users.Role;
import com.example.systembank.repository.UserRepository;
import com.example.systembank.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    private List<Role> roles;
    private List<AccountHolder> accHolders;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws ParseException {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //Roles
        Role adminRole = new Role(null, "ADMIN");
        Role accHRole = new Role(null, "ACCOUNT_HOLDER");
        roles = roleRepository.saveAll(List.of(adminRole, accHRole));

        //User Admin
        Admin admin = new Admin("Jose Luis", "jose", "1234", adminRole);
        userRepository.save(admin);

        //Account Holders
        AccountHolder accH1 = new AccountHolder("Yadira", "yadi", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("1985-11-10"), new Address("Ordos", 28911, "Madrid"), null);
        AccountHolder accH2 = new AccountHolder("Ernesto", "erne", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("1987-09-18"), new Address("Habana", 10100, "Havana"), null);
        AccountHolder accH3 = new AccountHolder("Camila", "cami", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-01"), new Address("Pavones", 28036, "Madrid"), null);
        accHolders = userRepository.saveAll(List.of(accH1, accH2, accH3));

    }

    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
        roleRepository.deleteAll();

    }


    @Test
    void createAccountHolder_valid() throws Exception {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Laura", "lary", "1234", "1980-12-30",
                new Address("Pavones", 28036, "Madrid"));
        String body = objectMapper.writeValueAsString(accountHolderDTO);
        mockMvc.perform(post("/api/account_holders")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

}