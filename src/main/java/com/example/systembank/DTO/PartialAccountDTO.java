package com.example.systembank.DTO;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PartialAccountDTO {

    @Digits(integer = 6,fraction = 4)
    private BigDecimal balance;

}
