package com.example.systembank.controller.impl;

import com.example.systembank.DTO.CheckingDTO;
import com.example.systembank.DTO.TransferAccToAccDTO;
import com.example.systembank.DTO.TransferAccToThirdPartyDTO;
import com.example.systembank.DTO.TransferThirdPartyToAccDTO;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.transactions.Transfer;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.Address;
import com.example.systembank.model.users.Admin;
import com.example.systembank.model.users.Role;
import com.example.systembank.repository.*;
import com.example.systembank.service.impl.AccountService;
import com.example.systembank.service.impl.TransferService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class TransferControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private List<Role> roles;
    private List<AccountHolder> accHolders;
    private List<Account> accounts;

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferRepository transferRepository;

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

        //Accounts
        Checking checking = new Checking(new Money(new BigDecimal(2000.0000)), accH1, null, "123");
        StudentChecking studentChecking = new StudentChecking(new Money(new BigDecimal(800.0000)), accH3, accH1, "321");
        Saving saving = new Saving(new Money(new BigDecimal(90.0000)), accH2, null, "333");
        accounts = accountRepository.saveAll(List.of(checking, studentChecking, saving));

        //ThirdParty
        ThirdParty thirdParty = new ThirdParty("3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2", "Pedro Perez");
        thirdPartyRepository.save(thirdParty);

        //Transfer
        Date date = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date = calendar.getTime();

        Transfer transfer1 = new Transfer(new Money(new BigDecimal(200)), date, checking, saving);
        Transfer transfer2 = new Transfer(new Money(new BigDecimal(300)), date, checking, null, null, thirdParty);
        Transfer transfer3 = new Transfer(new Money(new BigDecimal(100)), date, null, studentChecking, thirdParty, null);
        transferRepository.saveAll(List.of(transfer1, transfer2, transfer3));

    }

    @AfterEach
    void tearDown() {

        transferRepository.deleteAll();
        thirdPartyRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

    }

    @Test
    @WithMockUser(username = "yadi", password = "1234", roles = "ACCOUNT_HOLDER")
    void transferFromAccountToAccount_validate() throws Exception {

        /*Account sender = accounts.get(0);
        Long id = sender.getId();
        Account receiver = accounts.get(2);

        TransferAccToAccDTO transferAccToAccDTO = new TransferAccToAccDTO(new BigDecimal("300"), receiver);

        String body = objectMapper.writeValueAsString(transferAccToAccDTO);

        mockMvc.perform(post("/api/transfers/account-account/{id}", id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());*/
    }

    @Test
    @WithMockUser(username = "yadi", password = "1234", roles = "ACCOUNT_HOLDER")
    void transferFromAccountToThirdParty_validate() throws Exception {

        /*Account sender = accounts.get(0);
        Long id = sender.getId();

        TransferAccToThirdPartyDTO transferAccToThirdPartyDTO = new TransferAccToThirdPartyDTO(new BigDecimal("30"), "3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2");
        String body = objectMapper.writeValueAsString(transferAccToThirdPartyDTO);
        mockMvc.perform(post("/api/transfers/account-third_party/{id}", id)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());*/
    }

    @Test
    void transferFromThirdPartyToAccount_validate() throws Exception {

        /*Account receiver = accounts.get(0);
        Long id = receiver.getId();
        String secretKey = receiver.getSecretKey();


        TransferThirdPartyToAccDTO transferThirdPartyToAccDTO = new TransferThirdPartyToAccDTO(new BigDecimal("30"), id, secretKey);
        String body = objectMapper.writeValueAsString(transferThirdPartyToAccDTO);
        mockMvc.perform(post("/api/transfers/third_party-account/{hashedKey}", "3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());*/
    }
}