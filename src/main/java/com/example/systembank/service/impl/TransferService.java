package com.example.systembank.service.impl;

import com.example.systembank.DTO.TransferAccToAccDTO;
import com.example.systembank.DTO.TransferAccToThirdPartyDTO;
import com.example.systembank.DTO.TransferThirdPartyToAccDTO;
import com.example.systembank.enums.Status;
import com.example.systembank.model.accounts.*;
import com.example.systembank.model.transactions.Transfer;
import com.example.systembank.model.users.AccountHolder;
import com.example.systembank.repository.AccountRepository;
import com.example.systembank.repository.ThirdPartyRepository;
import com.example.systembank.repository.TransferRepository;
import com.example.systembank.service.interfaces.TransferServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransferService implements TransferServiceInterface {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ThirdPartyRepository thirdPartyRepository;

    @Override
    public void transferFromAccountToAccount(Long id, String username, TransferAccToAccDTO transferAccToAccDTO) {
        Optional<Account> senderAccountOp = accountRepository.findById(id);
        if(senderAccountOp.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sender account does not exist");
        } else {

            Account senderAccount = senderAccountOp.get();
            String userPrimOwn = senderAccount.getPrimaryOwner().getUsername();
            String userSecOwn = "";
            AccountHolder secondOwn = senderAccount.getSecondaryOwner();
            if(!(secondOwn == null)){
                userSecOwn = secondOwn.getUsername();
            }
            if(!username.equals(userPrimOwn) && !username.equals(userSecOwn)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user has not access to the account");
            } else {

                if("FROZEN".equals(senderAccount.getStatus().toString())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sender account is frozen");
                } else {
                    if(senderAccount.getBalance().getAmount().compareTo(transferAccToAccDTO.getAmount()) < 0){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not have enough money");
                    } else {
                        Optional<Account> receiverAccountOp = accountRepository.findById(transferAccToAccDTO.getReceiverA().getId());
                        if(receiverAccountOp.isEmpty()){
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receiver account does not exist");
                        } else {

                            Account receiverAccount = receiverAccountOp.get();
                            if("FROZEN".equals(receiverAccount.getStatus().toString())){
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The receiver account is frozen");
                            } else {
                                //Detect fraud
                                BigDecimal amount = transferAccToAccDTO.getAmount();

                                if(detectFraudPerDays(receiverAccount, amount) || detectFraudPerSeconds(receiverAccount)){
                                    receiverAccount.setStatus(Status.FROZEN);
                                    accountRepository.save(receiverAccount);

                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud attempt detected");

                                } else {

                                    //Money is subtracted from yhe sender
                                    decreaseSenderAccount(senderAccount, amount);

                                    //Money is added to the receiver
                                    increaseReceiverAccount(receiverAccount, amount);

                                    //Save transfer
                                    Transfer transfer = new Transfer(new Money(amount), senderAccount, receiverAccount);
                                    transferRepository.save(transfer);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void transferFromAccountToThirdParty(Long id, String username, TransferAccToThirdPartyDTO transferAccountToThirdPartyDTO) {
        Optional<Account> senderAccountOp = accountRepository.findById(id);
        if (senderAccountOp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sender account does not exist");
        } else {

            Account senderAccount = senderAccountOp.get();
            String userPrimOwn = senderAccount.getPrimaryOwner().getUsername();
            String userSecOwn = "";
            AccountHolder secondOwn = senderAccount.getSecondaryOwner();
            if (!(secondOwn == null)) {
                userSecOwn = secondOwn.getUsername();
            }
            if (!username.equals(userPrimOwn) && !username.equals(userSecOwn)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user has not access to the account");
            } else {

                if (senderAccount.getBalance().getAmount().compareTo(transferAccountToThirdPartyDTO.getAmount()) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not have enough money");
                } else {

                    if("FROZEN".equals(senderAccount.getStatus().toString())){
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sender account is frozen");
                    } else {

                        String hashedKey = transferAccountToThirdPartyDTO.getHashedKey();
                        Optional<ThirdParty> thirdPartyOp = thirdPartyRepository.findByHashedKey(hashedKey);
                        if (thirdPartyOp.isEmpty()) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The third-party does not exist");
                        } else {

                            //Money is subtracted from yhe sender
                            BigDecimal amount = transferAccountToThirdPartyDTO.getAmount();
                            decreaseSenderAccount(senderAccount, amount);

                            //Save transfer
                            ThirdParty receiverThirdParty = thirdPartyOp.get();
                            Transfer transfer = new Transfer(new Money(amount), senderAccount, null, null, receiverThirdParty);
                            transferRepository.save(transfer);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void transferFromThirdPartyToAccount(String hashedKey, TransferThirdPartyToAccDTO transferThirdPartyToAccDTO) {

        Optional<ThirdParty> thirdPartyOp = thirdPartyRepository.findByHashedKey(hashedKey);
        if (thirdPartyOp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The third-party does not exist");
        } else {
            Long id = transferThirdPartyToAccDTO.getIdAcc();
            String secretKey = transferThirdPartyToAccDTO.getSecretKeyAcc();
            Optional<Account> accountOp = accountRepository.findById(id);
            if(accountOp.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The account does not exist");
            } else{
                Account account = accountOp.get();
                if("FROZEN".equals(account.getStatus().toString())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The sender account is frozen");
                } else {
                      if(!(secretKey.equals(account.getSecretKey()))){
                          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong secretKey");
                    } else {

                        //Detect fraud per day amount
                        BigDecimal amount = transferThirdPartyToAccDTO.getAmount();
                        if(detectFraudPerDays(account, amount) || detectFraudPerSeconds(account)) {
                            account.setStatus(Status.FROZEN);
                            accountRepository.save(account);

                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud attempt detected");

                        } else {

                            //Increase amount of the account
                            increaseReceiverAccount(account, amount);

                            //Save transfer
                            ThirdParty thirdParty = thirdPartyOp.get();
                            Transfer transfer = new Transfer(new Money(amount), null, account,  thirdParty, null);
                            transferRepository.save(transfer);
                        }
                    }
                }
            }
        }
    }


    //============================ UTILITIES ==============================
    public void decreaseSenderAccount(Account senderAccount, BigDecimal amount){
        Money senderBalance = senderAccount.getBalance();
        senderBalance.decreaseAmount(amount);

        //Checking minimum balance if the account is checking type
        String senderAccountType = accountRepository.findTypeAccountById(senderAccount.getId()).toString();

        if(senderAccountType.equals("checking")){

            Checking senderCheckingType = (Checking)senderAccount;
            BigDecimal penalty = senderCheckingType.getPenaltyFee();
            if(senderBalance.getAmount().compareTo(penalty) < 0){
                senderBalance.decreaseAmount(penalty);
            }

            senderAccount.setBalance(senderBalance);
            accountRepository.save(senderCheckingType);
        }

        //Checking minimum balance if the account saving type
        if(senderAccountType.equals("saving")){

            Saving senderSavingType = (Saving) senderAccount;
            BigDecimal penalty = senderSavingType.getPenaltyFee();
            if(senderBalance.getAmount().compareTo(penalty) < 0){
                senderBalance.decreaseAmount(penalty);
            }

            senderAccount.setBalance(senderBalance);
            accountRepository.save(senderSavingType);
        }

        //Just subtract for student checking type
        senderAccount.setBalance(senderBalance);
        accountRepository.save(senderAccount);
    }

    public void increaseReceiverAccount(Account receiverAccount, BigDecimal amount){
        Money receiverBalance = receiverAccount.getBalance();
        receiverBalance.increaseAmount(amount);
        receiverAccount.setBalance(receiverBalance);
        accountRepository.save(receiverAccount);
    }

    public boolean detectFraudPerDays( Account account, BigDecimal amount ){

        List<Transfer> transfers = transferRepository.findAll();
        Map<String, BigDecimal> days = new HashMap<>();

        boolean isEmpty = true;

        for (Transfer transfer : transfers) {

            Account receiver = transfer.getReceiverA();
            if(receiver != null) {
                if (Objects.equals(receiver.getId(), account.getId())) {

                    String date = transfer.getDate().toString().substring(0, 10);
                    BigDecimal amountPerDay = transfer.getAmount().getAmount();

                    if (isEmpty) {
                        days.put(date, amountPerDay);
                        isEmpty = false;
                    } else {
                        days.merge(date, amountPerDay, BigDecimal::add);
                    }
                }
            }
        }

        BigDecimal max = new BigDecimal(0);

        for (BigDecimal value : days.values()) {
            max = max.compareTo(value) > 0 ? max : value;
        }

        BigDecimal permitAmount = max.multiply(new BigDecimal(1.5));
        if(permitAmount.compareTo(amount) < 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean detectFraudPerSeconds( Account account ){

        Date now = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, -1);

        Date lastSecond = calendar.getTime();

        List<Transfer> transfers = transferRepository.findAllByDateBetween(lastSecond, now);
        int count = 0;
        if(transfers.size() > 0){
            for(Transfer transfer : transfers){
                Account receiver = transfer.getReceiverA();
                if(receiver != null) {

                    if (Objects.equals(receiver.getId(), account.getId())) {
                        count++;
                    }

                }
            }
        }

        if (count > 1){
            return true;
        } else {
            return false;
        }
    }
}
