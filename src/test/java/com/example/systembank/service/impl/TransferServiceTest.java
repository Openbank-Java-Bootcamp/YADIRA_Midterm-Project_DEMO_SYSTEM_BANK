package com.example.systembank.service.impl;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransferServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

    private List<Account> accounts;

    @BeforeEach
    void setUp() throws ParseException {
        //Roles
        Role adminRole = new Role(null, "ADMIN");
        Role accHRole = new Role(null, "ACCOUNT_HOLDER");
        roleRepository.saveAll(List.of(adminRole, accHRole));

        //User Admin
        Admin admin = new Admin("Jose Luis", "jose", "1234", adminRole);
        userRepository.save(admin);

        //Account Holders
        AccountHolder accH1 = new AccountHolder("Yadira", "yadi", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("1985-11-10"), new Address("Ordos", 28911, "Madrid"), null);
        AccountHolder accH2 = new AccountHolder("Ernesto", "erne", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("1987-09-18"), new Address("Habana", 10100, "Havana"), null);
        AccountHolder accH3 = new AccountHolder("Camila", "cami", "1234", accHRole, new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-01"), new Address("Pavones", 28036, "Madrid"), null);
        userRepository.saveAll(List.of(accH1, accH2, accH3));

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
    void transferFromAccountToAccount_validate() {

        String username = "yadi";

        Account sender = accounts.get(0);
        Long id = sender.getId();

        Account receiver = accounts.get(2);

        TransferAccToAccDTO transferAccToAccDTO = new TransferAccToAccDTO(new BigDecimal(10), receiver);

        transferService.transferFromAccountToAccount(id, username, transferAccToAccDTO);
        assertEquals(4L, transferRepository.count());

        Account senderFromDB = accountRepository.findById(id).get();
        BigDecimal senderBalance = senderFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("1990.00"), senderBalance);

        Account receiverFromDB = accountRepository.findById(receiver.getId()).get();
        BigDecimal receiverBalance = receiverFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("100.00"), receiverBalance);

    }

    @Test
    void transferFromAccountToThirdParty_validate() {

        String username = "yadi";

        Account sender = accounts.get(0);
        Long id = sender.getId();

        TransferAccToThirdPartyDTO transferAccToThirdPartyDTO = new TransferAccToThirdPartyDTO(new BigDecimal(30), "3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2");

        transferService.transferFromAccountToThirdParty(id, username, transferAccToThirdPartyDTO);
        assertEquals(4L, transferRepository.count());

        Account senderFromDB = accountRepository.findById(id).get();
        BigDecimal senderBalance = senderFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("1970.00"), senderBalance);

    }

    @Test
    void transferFromThirdPartyToAccount_validate() {

        String hashedKey = "3721ceb154c49efcb0d5a425c68410197f3872651df9fc37724cf9a9cda6c1e2";

        Account receiver = accounts.get(1);
        Long id = receiver.getId();
        String secretKey = receiver.getSecretKey();

        TransferThirdPartyToAccDTO transferThirdPartyToAccDTO = new TransferThirdPartyToAccDTO(new BigDecimal(50), id, secretKey);

        transferService.transferFromThirdPartyToAccount(hashedKey, transferThirdPartyToAccDTO);
        assertEquals(4L, transferRepository.count());

        Account receiverFromDB = accountRepository.findById(receiver.getId()).get();
        BigDecimal receiverBalance = receiverFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("850.00"), receiverBalance);

    }

    @Test
    void decreaseSenderAccount_validate() {

        Account account = accounts.get(0);
        transferService.decreaseSenderAccount(account, new BigDecimal(1000));

        Account accountFromDB = accountRepository.findById(account.getId()).get();
        BigDecimal balance = accountFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("1000.00"), balance);

    }

    @Test
    void increaseReceiverAccount_validate() {

        Account account = accounts.get(0);
        transferService.increaseReceiverAccount(account, new BigDecimal(1000));

        Account accountFromDB = accountRepository.findById(account.getId()).get();
        BigDecimal balance = accountFromDB.getBalance().getAmount();
        assertEquals(new BigDecimal("3000.00"), balance);
    }

}