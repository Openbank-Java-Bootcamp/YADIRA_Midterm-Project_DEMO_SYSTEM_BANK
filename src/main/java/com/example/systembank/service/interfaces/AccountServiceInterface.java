package com.example.systembank.service.interfaces;

import com.example.systembank.DTO.*;
import com.example.systembank.model.accounts.Account;
import com.example.systembank.model.accounts.CreditCard;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;


public interface AccountServiceInterface {
    void createCheckingAccount(CheckingDTO checkingDTO);
    void createSavingAccount(SavingDTO savingDTO);
    void createCreditCard(CreditCardDTO creditCardDTO);
    void createThirdParty(ThirdPartyDTO thirdPartyDTO) throws NoSuchAlgorithmException;
    BigDecimal consultBalance(Long id, String username);
    BigDecimal consultBalanceCreditCard(Long id, String username);
    void increaseBalance(Long id, PartialAccountDTO partialAccountDTO);
    void deleteAccount(Long id);
}
