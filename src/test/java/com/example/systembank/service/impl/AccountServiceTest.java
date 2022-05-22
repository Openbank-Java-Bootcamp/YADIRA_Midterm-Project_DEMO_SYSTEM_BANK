package com.example.systembank.service.impl;

import com.example.systembank.DTO.*;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.users.*;
import com.example.systembank.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

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
    private CreditCard creditCardSave;

    @BeforeEach
    void setUp() throws ParseException {

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
        creditCardSave = creditCardRepository.save(creditCard);

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
    void createCheckingAccount_validate_StudentAccountType() {

        AccountHolder accH = accHolders.get(2);
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal(500), accH, null, "123");
        accountService.createCheckingAccount(checkingDTO);

        assertEquals(4L, accountRepository.count());

        List<Account> accounts = accountRepository.findAll();
        Account lastAcc = accounts.get(accounts.size() - 1);
        Long lastId = lastAcc.getId();

        assertEquals("student_checking", accountRepository.findTypeAccountById(lastId));

    }

    @Test
    void createCheckingAccount_validate_CheckingType() {

        AccountHolder accH = accHolders.get(0);
        CheckingDTO checkingDTO = new CheckingDTO(new BigDecimal(500), accH, null, "123");
        accountService.createCheckingAccount(checkingDTO);

        assertEquals(4L, accountRepository.count());

        List<Account> accounts = accountRepository.findAll();
        Account lastAcc = accounts.get(accounts.size() - 1);
        Long lastId = lastAcc.getId();

        assertEquals("checking", accountRepository.findTypeAccountById(lastId));

    }

    @Test
    void createSavingAccount_validate() {

        AccountHolder accH = accHolders.get(1);
        SavingDTO savingDTO = new SavingDTO(new BigDecimal(600), accH, null, "123", new BigDecimal(1000), new BigDecimal(0.5));

        accountService.createSavingAccount(savingDTO);

        assertEquals(4L, accountRepository.count());

    }


    @Test
    void createCreditCard_validate() {

        AccountHolder accH = accHolders.get(0);

        CreditCardDTO creditCardDTO = new CreditCardDTO(new BigDecimal(600), accH, null);

        accountService.createCreditCard(creditCardDTO);

        assertEquals(2L, creditCardRepository.count());

    }

    @Test
    void createThirdParty_validate() throws NoSuchAlgorithmException {

        ThirdPartyDTO thirdPartyDTO = new ThirdPartyDTO("Luis Benitez");

        accountService.createThirdParty(thirdPartyDTO);

        assertEquals(2L, thirdPartyRepository.count());
    }


    @Test
    void consultBalance_validate_primaryOwner() {

        String username = "cami";
        Account account = accounts.get(1);
        Long id = account.getId();

        BigDecimal balance = accountService.consultBalance(id, username);

        assertEquals(new BigDecimal("800.00"), balance);

    }

    @Test
    void consultBalance_validate_secondaryOwner() {

        String username = "yadi";
        Account account = accounts.get(1);
        Long id = account.getId();

        BigDecimal balance = accountService.consultBalance(id, username);

        assertEquals(new BigDecimal("800.00"), balance);

    }

    @Test
    void consultBalanceCreditCard_validate() {

        String username = "cami";
        Long id = creditCardSave.getId();

        BigDecimal balance = accountService.consultBalanceCreditCard(id, username);
        balance = balance.setScale(2, RoundingMode.HALF_EVEN);

        assertEquals(new BigDecimal("2300.00"), balance);

    }

    @Test
    void increaseBalance_validate() {

        PartialAccountDTO partialAccountDTO = new PartialAccountDTO(new BigDecimal(200));
        Account account = accounts.get(0);
        Long id = account.getId();

        accountService.increaseBalance(id, partialAccountDTO);

        BigDecimal balance = accountRepository.findById(id).get().getBalance().getAmount();

        assertEquals(new BigDecimal("2200.00"), balance);
    }

    @Test
    void deleteAccount_validate() {

        Account account = accounts.get(1);
        Long id = account.getId();

        accountService.deleteAccount(id);

        Optional<Account> accountDeleted = accountRepository.findById(id);

        assertTrue(accountDeleted.isEmpty());

    }

    @Test
    void changeStatus_validate() {

        Account account = accounts.get(1);
        Long id = account.getId();

        String status = "FROZEN";

        accountService.changeStatus(id, status);

        assertEquals(status, accountRepository.findById(id).get().getStatus().toString());

    }

}