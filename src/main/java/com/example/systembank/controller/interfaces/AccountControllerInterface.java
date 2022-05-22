package com.example.systembank.controller.interfaces;

import com.example.systembank.DTO.*;
import com.example.systembank.model.accounts.Account;
import com.example.systembank.model.accounts.CreditCard;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

public interface AccountControllerInterface {

   void createCheckingAccount(CheckingDTO checkingDTO);
   void createSavingAccount(SavingDTO savingDTO);
   void createCreditCard(CreditCardDTO creditCardDTO);
   void createThirdParty(ThirdPartyDTO thirdPartyDTO);
   BigDecimal consultBalanceAccount(Long id);
   BigDecimal consultBalanceCreditCard(Long id);
   void increaseBalance(Long id, PartialAccountDTO partialAccountDTO);
   void deleteAccount(Long id);

}
