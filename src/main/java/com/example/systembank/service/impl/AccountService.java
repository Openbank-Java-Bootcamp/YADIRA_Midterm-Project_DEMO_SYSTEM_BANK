package com.example.systembank.service.impl;

import com.example.systembank.DTO.*;
import com.example.systembank.enums.Status;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.model.users.User;
import com.example.systembank.repository.UserRepository;
import com.example.systembank.repository.AccountRepository;
import com.example.systembank.repository.CreditCardRepository;
import com.example.systembank.repository.ThirdPartyRepository;
import com.example.systembank.service.interfaces.AccountServiceInterface;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
public class AccountService implements AccountServiceInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    //=========================== CREATE ACCOUNTS ===================================

    @Override
    public void createCheckingAccount(CheckingDTO checkingDTO){
        User accHolderPrimary = null;
        User accHolderSecondary = null;

        Optional<User> accHolderPrimaryOptional = userRepository.findById(checkingDTO.getPrimaryOwner().getId());

        if(checkingDTO.getSecondaryOwner() != null){
            Optional<User> accHolderSecondaryOptional = userRepository.findById(checkingDTO.getSecondaryOwner().getId());
            if(accHolderSecondaryOptional.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secondary owner is incorrect");
            }
            if("ADMIN".equals(accHolderSecondaryOptional.get().getRole().getName())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
            }
            accHolderSecondary = accHolderSecondaryOptional.get();
        }

        if(accHolderPrimaryOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The owner of the account is incorrect");
        } else if("ADMIN".equals(accHolderPrimaryOptional.get().getRole().getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
        } else {
            accHolderPrimary = accHolderPrimaryOptional.get();
            if (Objects.equals(accHolderPrimary, accHolderSecondary)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The primary owner and the secondary cannot be the same");
            } else {
                int age = calculateAge(((AccountHolder)accHolderPrimary).getDateOfBirth());
                if (age >= 24) {
                    Checking checking = new Checking(new Money(checkingDTO.getBalance()), (AccountHolder)accHolderPrimary, (AccountHolder)accHolderSecondary,
                            checkingDTO.getSecretKey());

                    if (checking.getBalance().getAmount().compareTo(checking.getMinimumBalance()) < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The minimum balance is not correct");
                    } else {
                        accountRepository.save(checking);
                    }
                } else {

                    StudentChecking studentChecking = new StudentChecking(
                            new Money(checkingDTO.getBalance()), (AccountHolder)accHolderPrimary, (AccountHolder)accHolderSecondary,
                            checkingDTO.getSecretKey());
                    accountRepository.save(studentChecking);
                }
            }
        }

    }

    @Override
    public void createSavingAccount(SavingDTO savingDTO) {

        AccountHolder accHolderPrimary = null;
        AccountHolder accHolderSecondary = null;

        Optional<User> accHolderPrimaryOptional = userRepository.findById(savingDTO.getPrimaryOwner().getId());

        if(accHolderPrimaryOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The owner of the account is incorrect");
        }
        if("ADMIN".equals(accHolderPrimaryOptional.get().getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
        }
        if (savingDTO.getSecondaryOwner() != null){
            Optional<User> accHolderSecondaryOptional = userRepository.findById(savingDTO.getSecondaryOwner().getId());
            if(accHolderSecondaryOptional.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secondary owner is incorrect");
            }
            if("ADMIN".equals(accHolderSecondaryOptional.get().getRole().getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
            }
            if(Objects.equals(accHolderPrimaryOptional, accHolderSecondaryOptional)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The primary owner and the secondary cannot be the same");
            } else {
                accHolderSecondary = (AccountHolder)accHolderSecondaryOptional.get();
            }
        }
        accHolderPrimary = (AccountHolder)accHolderPrimaryOptional.get();
        Saving accountSaving = new Saving(new Money(savingDTO.getBalance()), accHolderPrimary, accHolderSecondary,
                savingDTO.getSecretKey(), savingDTO.getMinimumBalance(), savingDTO.getInterestRate()
        );

        accountRepository.save(accountSaving);

    }


    @Override
    public void createCreditCard(CreditCardDTO creditCardDTO) {

        AccountHolder accHolderPrimary = null;
        AccountHolder accHolderSecondary = null;

        Optional<User> accHolderPrimaryOptional = userRepository.findById(creditCardDTO.getPrimaryOwner().getId());

        if(accHolderPrimaryOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The owner of the account is incorrect");
        } else {
            if ("ADMIN".equals(accHolderPrimaryOptional.get().getRole().getName())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
            } else {
                if (creditCardDTO.getSecondaryOwner() != null) {
                    Optional<User> accHolderSecondaryOptional = userRepository.findById(creditCardDTO.getSecondaryOwner().getId());
                    if (accHolderSecondaryOptional.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The secondary owner is incorrect");
                    }
                    if ("ADMIN".equals(accHolderSecondaryOptional.get().getRole().getName())) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An admin cannot be a owner");
                    }
                    if (Objects.equals(accHolderPrimaryOptional, accHolderSecondaryOptional)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The primary owner and the secondary cannot be the same");
                    } else {
                        accHolderSecondary = (AccountHolder) accHolderSecondaryOptional.get();
                    }
                }
                accHolderPrimary = (AccountHolder) accHolderPrimaryOptional.get();
                CreditCard creditCard = new CreditCard(new Money(creditCardDTO.getBalance()), accHolderPrimary, accHolderSecondary,
                        creditCardDTO.getCreditLimit(), creditCardDTO.getInterestRate());

                creditCardRepository.save(creditCard);
            }
        }
    }

    @Override
    public void createThirdParty(ThirdPartyDTO thirdPartyDTO) throws NoSuchAlgorithmException {
        String hashKey = UUID.randomUUID().toString();
        //Optional<ThirdParty> thirdPartyOp = thirdPartyRepository.findByName(thirdPartyDTO.getName());
        String hashedKey;
        //if (thirdPartyOp.isPresent()) {
          //  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The Third-Party already exist");
        //} else {
            hashedKey = hashKey(hashKey);
            ThirdParty thirdParty = new ThirdParty(
                    hashedKey,
                    thirdPartyDTO.getName()
            );
            thirdPartyRepository.save(thirdParty);
        //}

    }


    //=========================== GET BALANCES =============================

    @Override
    public BigDecimal consultBalance(Long id, String username) {

        Optional<Account> accountOpp = accountRepository.findById(id);
        String type = "";
        BigDecimal finalBalance = null;

        if(accountOpp.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not exist");
        } else {
            type = accountRepository.findTypeAccountById(id).toString();

            String ownerUsername = accountOpp.get().getPrimaryOwner().getUsername();
            String secondOwnerUsername = "";
            AccountHolder secondOwner = accountOpp.get().getSecondaryOwner();
            if(!(secondOwner == null)){
                secondOwnerUsername = secondOwner.getUsername();
            }
            if(!username.equals(ownerUsername) && !username.equals(secondOwnerUsername)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user has not access to the account");
            } else {

                if (type.equals("student_checking")) {
                    finalBalance = accountOpp.get().getBalance().getAmount();
                }

                if (type.equals("checking")) {
                    Checking checking = (Checking) accountOpp.get();

                    //Calculate Monthly monthlyMaintenanceFee
                    Money balance = checking.getBalance();
                    int months = calculateMonths(checking.getLastConsultation());
                    BigDecimal monthlyMain = checking.getMonthlyMaintenanceFee()
                            .multiply(new BigDecimal(months));
                    balance.decreaseAmount(monthlyMain);

                    //PenaltyFee if balance fewer than minimum balance
                    if (balance.getAmount().compareTo(checking.getMinimumBalance()) < 0) {
                        balance.decreaseAmount(checking.getPenaltyFee());
                    }

                    //Actualize balance
                    checking.setBalance(balance);
                    finalBalance = balance.getAmount();

                    //Actualize lastConsultation
                    checking.setLastConsultation(Calendar.getInstance().getTime());

                    //Save changes
                    accountRepository.save(checking);
                }

                if (type.equals("saving")) {
                    Saving saving = (Saving) accountOpp.get();

                    //Calculate annual interest
                    Money balance = saving.getBalance();
                    int years = calculateAge(saving.getLastConsultation());
                    BigDecimal interest = saving.getInterestRate()
                            .multiply(new BigDecimal(years))
                            .multiply(balance.getAmount());
                    balance.increaseAmount(interest);

                    //PenaltyFee if balance fewer than minimum balance
                    if (balance.getAmount().compareTo(saving.getMinimumBalance()) < 0) {
                        balance.decreaseAmount(saving.getPenaltyFee());
                    }

                    //Actualize balance
                    saving.setBalance(balance);
                    finalBalance = balance.getAmount();

                    //Actualize lastConsultation
                    saving.setLastConsultation(Calendar.getInstance().getTime());

                    //Save changes
                    accountRepository.save(saving);
                }
            }
        }

        return finalBalance;
    }

    @Override
    public BigDecimal consultBalanceCreditCard(Long id, String username) {

        Optional<CreditCard> creditCardOpp = creditCardRepository.findById(id);
        BigDecimal finalBalance = null;

        if(creditCardOpp.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The credit card does not exist");
        } else {

            String ownerUsername = creditCardOpp.get().getPrimaryOwner().getUsername();
            String secondOwnerUsername = "";
            AccountHolder secondOwner = creditCardOpp.get().getSecondaryOwner();
            if (!(secondOwner == null)) {
                secondOwnerUsername = secondOwner.getUsername();
            }
            if (!username.equals(ownerUsername) && !username.equals(secondOwnerUsername)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user has not access to the account");
            } else {

                CreditCard creditCard = creditCardOpp.get();

                //Calculate Monthly monthlyMaintenanceFee
                Money balance = creditCard.getBalance();
                int months = calculateMonths(creditCard.getLastConsultation());
                BigDecimal monthlyInterest = creditCard.getInterestRate()
                        .divide(new BigDecimal(12), RoundingMode.HALF_EVEN);
                BigDecimal finalInterest = monthlyInterest
                        .multiply(new BigDecimal(months))
                        .multiply(balance.getAmount());
                balance.increaseAmount(finalInterest);

                //Actualize balance
                creditCard.setBalance(balance);
                finalBalance = balance.getAmount();

                //Actualize lastConsultation
                creditCard.setLastConsultation(Calendar.getInstance().getTime());

                //Save changes
                creditCardRepository.save(creditCard);

            }
        }

        return finalBalance;
    }

    // ========================== INCREASE A BALANCE ============================
    @Override
    public void increaseBalance(Long id, PartialAccountDTO partialAccountDTO) {
        Optional<Account> accountOpp = accountRepository.findById(id);

        if (accountOpp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not exist");
        } else {
            Account account = accountOpp.get();
            Money balance = account.getBalance();
            balance.increaseAmount(partialAccountDTO.getBalance());
            account.setBalance(balance);

            accountRepository.save(account);

        }
    }

    //=========================== DELETE AN ACCOUNT =======================

    @Override
    public void deleteAccount(Long id) {
        Optional<Account> accountOpp = accountRepository.findById(id);

        if (accountOpp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not exist");
        } else {
            accountRepository.delete(accountOpp.get());
        }
    }

    //========================= CHANGE STATUS ========================

    public void changeStatus(Long id, String status){

        Optional<Account> accountOp = accountRepository.findById(id);

        if(accountOp.isEmpty()){
            throw new UsernameNotFoundException( "No account was found." );
        } else {
            Account account = accountOp.get();
            if ("FROZEN".equals(status)) {
                account.setStatus(Status.FROZEN);
            } else {
                account.setStatus(Status.ACTIVE);
            }
            accountRepository.save(account);
        }
    }

    //============================ UTILITIES ===============================

    public static String hashKey(String originalKey) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest( originalKey.getBytes(StandardCharsets.UTF_8) );
        return new String(Hex.encode(hash));
    }

    public static int calculateAge (Date date){
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(date);
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- birthday.get(Calendar.YEAR);
        int month =today.get(Calendar.MONTH)- birthday.get(Calendar.MONTH);
        int day = today.get(Calendar.DATE)- birthday.get(Calendar.DATE);
        if(month<0 || (month==0 && day<0)){
            year--;
        }
        return year;
    }

    public static int calculateMonths (Date date){
        Calendar lastConsultation = Calendar.getInstance();
        lastConsultation.setTime(date);
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- lastConsultation.get(Calendar.YEAR);
        int month =today.get(Calendar.MONTH)- lastConsultation.get(Calendar.MONTH);
        /*int day = today.get(Calendar.DATE)- lastConsultation.get(Calendar.DATE);
        if(month<0 || (month==0 && day<0)){
            year--;
        }*/
        return year * 12 + month;
    }

}
