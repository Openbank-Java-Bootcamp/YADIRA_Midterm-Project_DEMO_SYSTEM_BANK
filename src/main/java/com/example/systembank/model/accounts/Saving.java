package com.example.systembank.model.accounts;


import javax.persistence.*;
import javax.validation.constraints.Digits;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value="saving")
public class Saving extends Account {

    @Digits(integer = 6,fraction = 4)
    private BigDecimal minimumBalance = new BigDecimal("1000");

    @Digits(integer = 6,fraction = 4)
    private BigDecimal interestRate = new BigDecimal("0.0025");

    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey, BigDecimal minimumBalance, BigDecimal interestRate) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
    }

    public Saving(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
    }
}
