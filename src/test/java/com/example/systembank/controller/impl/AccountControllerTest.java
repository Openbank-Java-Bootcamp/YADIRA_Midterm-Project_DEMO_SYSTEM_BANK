package com.example.systembank.controller.impl;

import com.example.systembank.DTO.*;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.users.*;
import com.example.systembank.repository.*;
import com.example.systembank.service.impl.AccountService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    private List<Role> roles;
    private List<AccountHolder> accHolders;
    private List<Account> accounts;

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

        //CreditCard
        CreditCard creditCard = new CreditCard(new Money(new BigDecimal(2300.0000)), accH3, null);
        creditCardRepository.save(creditCard);

        //ThirdParty
        ThirdParty thirdParty = new ThirdParty("3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2", "Pedro Perez");
        thirdPartyRepository.save(thirdParty);

    }

    @AfterEach
    void tearDown() {

        thirdPartyRepository.deleteAll();
        creditCardRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

    }

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void createCheckingAccount_validate() throws Exception {

        AccountHolder accH = accHolders.get(2);
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal("2100.0000"), accH, null, "111");

        String body = objectMapper.writeValueAsString(checkingDTO);
        mockMvc.perform(post("/api/accounts/checkings")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        accountService.createCheckingAccount(checkingDTO);

    }

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void createSavingAccount_validate() throws Exception {

        AccountHolder accH = accHolders.get(1);
        SavingDTO savingDTO = new SavingDTO(new BigDecimal(600), accH, null, "123", new BigDecimal(1000), new BigDecimal(0.5));

        String body = objectMapper.writeValueAsString(savingDTO);
        mockMvc.perform(post("/api/accounts/savings")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        accountService.createSavingAccount(savingDTO);

    }

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void createCreditCard_validate() throws Exception {

        AccountHolder accH = accHolders.get(0);
        CreditCardDTO creditCardDTO = new CreditCardDTO(new BigDecimal("100.0000"), accH, null);

        String body = objectMapper.writeValueAsString(creditCardDTO);
        mockMvc.perform(post("/api/accounts/credit_cards")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        accountService.createCreditCard(creditCardDTO);
    }

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void createThirdParty_validate() throws Exception {

        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Ricardo de Leon");

        String body = objectMapper.writeValueAsString(thirdPartyDTO);
        mockMvc.perform(post("/api/accounts/third_parties")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
    }

 /*   @Test
    @WithMockUser(username = "cami", password = "1234", roles = "ACCOUNT_HOLDER")
    void consultBalanceAccount_validate() throws Exception {

        Account account = accounts.get(1);
        Long id = account.getId();

        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("800.00"));

    }

    @Test
    @WithMockUser(username = "cami", password = "1234", roles = "ACCOUNT_HOLDER")
    void consultBalanceCreditCard_validate() throws Exception {



        MvcResult mvcResult = mockMvc.perform(get("/api/accounts/credit_card/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("4571"));

    }*/


    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void increaseBalance_validate() throws Exception {

        Account account = accounts.get(1);
        Long id = account.getId();

        PartialAccountDTO partialAccountDTO = new PartialAccountDTO(new BigDecimal("50"));
        mockMvc.perform(patch("/api/accounts/{id}", id)
                .content(asJsonString(partialAccountDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "jose", password = "1234", roles = "ADMIN")
    void deleteAccount_validate() throws Exception {

        Account account = accounts.get(1);
        Long id = account.getId();

        mockMvc.perform(delete("/api/accounts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}