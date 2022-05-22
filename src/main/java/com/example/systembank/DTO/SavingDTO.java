package com.example.systembank.DTO;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SavingDTO {

    @NotNull
    @Digits(integer = 6,fraction = 2)
    private BigDecimal balance;

    @NotNull(message = "The account must have a owner")
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    @NotNull(message = "SecretKey cannot be null")
    @Pattern(regexp = "[0-9]{3}")
    private String secretKey;

    @DecimalMin(value = "100", message = "The minimum balance cannot no be fewer than 100")
    @DecimalMax(value = "1000", message = "The minimum balance cannot no be greater than 1000")
    private BigDecimal minimumBalance = new BigDecimal("1000");

    @Digits(integer = 6,fraction = 4)
    @DecimalMax(value = "0.5", message = "The interest rate cannot be greater than 0.5")
    private BigDecimal interestRate = new BigDecimal("0.0025");

}
