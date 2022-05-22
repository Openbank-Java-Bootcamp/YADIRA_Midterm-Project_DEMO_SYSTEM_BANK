package com.example.systembank.model.accounts;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DiscriminatorValue(value="checking")
public class Checking extends Account {

    @Digits(integer = 6,fraction = 4)
    private BigDecimal minimumBalance = new BigDecimal("250");

    @Digits(integer = 6,fraction = 4)
    private BigDecimal monthlyMaintenanceFee = new BigDecimal("12");

    public Checking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
    }

}
