package com.example.systembank.DTO;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CheckingDTO {

    @NotNull
    @Digits(integer = 6,fraction = 4)
    private BigDecimal balance;

    @NotNull(message = "The account must have a owner")
    private AccountHolder primaryOwner;

    private AccountHolder secondaryOwner;

    @NotNull(message = "SecretKey cannot be null")
    @Pattern(regexp = "[0-9]{3}")
    private String secretKey;


}
