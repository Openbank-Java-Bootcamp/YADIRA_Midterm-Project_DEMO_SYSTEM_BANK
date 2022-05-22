package com.example.systembank.DTO;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreditCardDTO {

    @Digits(integer = 6,fraction = 4)
    private BigDecimal balance;

    @NotNull(message = "The account must have a owner")
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    @DecimalMin(value = "100", message = "The credit card limit cannot be fewer than 100")
    @DecimalMax(value = "100000", message = "The credit card limit cannot be greater than 100000")
    private BigDecimal creditLimit = new BigDecimal("100");;

    @DecimalMin(value = "0.1", message = "The minimum value permitted for the interestRate is 0.1")
    @DecimalMax(value = "0.2", message = "The maximum value permitted for the interestRate is 0.2")
    private BigDecimal interestRate = new BigDecimal("0.2");

    @Temporal(TemporalType.DATE)
    private Date lastInterestAdding;

    public CreditCardDTO(BigDecimal balance, AccountHolder primaryOwner, AccountHolder secondaryOwner) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
    }
}
