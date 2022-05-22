package com.example.systembank.model.accounts;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue(value="credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Money balance;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id", referencedColumnName = "id", nullable = false)
    protected AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id", referencedColumnName = "id")
    private AccountHolder secondaryOwner;

    @Digits(integer = 6,fraction = 4)
    private BigDecimal creditLimit = new BigDecimal("100");

    @Digits(integer = 6,fraction = 4)
    private BigDecimal interestRate = new BigDecimal("0.2");

    @Temporal(TemporalType.DATE)
    private Date lastConsultation = Calendar.getInstance().getTime();

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit, BigDecimal interestRate, Date lastConsultation) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
        this.lastConsultation = lastConsultation;
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, BigDecimal creditLimit, BigDecimal interestRate) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.creditLimit = creditLimit;
        this.interestRate = interestRate;
    }

    public CreditCard(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }
}

