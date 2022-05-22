package com.example.systembank.service.impl;

import com.example.systembank.DTO.AccountHolderDTO;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.Address;
import com.example.systembank.model.users.Admin;
import com.example.systembank.model.users.Role;
import com.example.systembank.repository.RoleRepository;
import com.example.systembank.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() throws ParseException {

        //Roles
        roleRepository.save(new Role(null, "ACCOUNT_HOLDER"));
        Role adminRole = new Role(null, "ADMIN");
        roleRepository.save(adminRole);

        //User Admin
        Admin admin = new Admin("Jose Luis", "jose", "1234", adminRole);
        userRepository.save(admin);

        //Account Holders
        AccountHolder accH1 = new AccountHolder("Yadira", "yadi", "1234", new SimpleDateFormat("yyyy-MM-dd").parse("1985-11-10"), new Address("Ordos", 28911, "Madrid"), null);
        AccountHolder accH2 = new AccountHolder("Camila", "cami", "1234", new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-01"), new Address("Pavones", 28036, "Madrid"), null);
        AccountHolder accH3 = new AccountHolder("Ernesto", "erne", "1234", new SimpleDateFormat("yyyy-MM-dd").parse("1987-09-18"), new Address("Habana", 10100, "Havana"), null);
        userRepository.saveAll(List.of(accH1, accH2, accH3));
    }


    @AfterEach
    void tearDown() {

        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void saveRole_validate() throws Exception {

        Role role = new Role(3L, "ANALYST");
        userService.saveRole(role);

        assertEquals(3L, roleRepository.count());

    }

    @Test
    void createAccountHolder_validate() throws Exception {
        AccountHolderDTO accountHolderDTO = new AccountHolderDTO("Andres Tenorio", "andres", "1234",
                "1985-02-01", new Address("Pavones", 28036, "Madrid"));

        userService.createAccountHolder(accountHolderDTO);

        assertEquals(5L, userRepository.count());
    }
}