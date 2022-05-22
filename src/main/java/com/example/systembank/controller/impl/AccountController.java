package com.example.systembank.controller.impl;

import com.example.systembank.DTO.*;
import com.example.systembank.controller.interfaces.AccountControllerInterface;
import com.example.systembank.repository.ThirdPartyRepository;
import com.example.systembank.service.impl.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/accounts")
public class AccountController implements AccountControllerInterface {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ThirdPartyRepository repository;

    @PostMapping("/checkings")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCheckingAccount(@RequestBody @Valid CheckingDTO checkingDTO) {
        accountService.createCheckingAccount(checkingDTO);
    }

    @PostMapping("/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSavingAccount(@RequestBody @Valid SavingDTO savingDTO) {
        accountService.createSavingAccount(savingDTO);
    }

    @PostMapping("/credit_cards")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCreditCard(@RequestBody @Valid CreditCardDTO creditCardDTO) {
        accountService.createCreditCard(creditCardDTO);
    }

    @PostMapping("/third_parties")
    @ResponseStatus(HttpStatus.CREATED)
    public void createThirdParty(@RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        try {
            accountService.createThirdParty(thirdPartyDTO);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal consultBalanceAccount(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        return accountService.consultBalance(id, username);
    }

    @GetMapping("/credit_card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal consultBalanceCreditCard(@PathVariable(name = "id") Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();
        return accountService.consultBalanceCreditCard(id, username);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void increaseBalance(@PathVariable Long id,@RequestBody PartialAccountDTO partialAccountDTO){
        accountService.increaseBalance(id,partialAccountDTO);
    }

    @DeleteMapping ("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
    }

    @PatchMapping("/status/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@PathVariable Long id,@RequestParam String status){
        accountService.changeStatus(id, status);
    }

}
