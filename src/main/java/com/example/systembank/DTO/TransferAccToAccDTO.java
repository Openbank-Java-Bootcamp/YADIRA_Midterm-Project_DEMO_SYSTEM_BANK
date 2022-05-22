package com.example.systembank.DTO;

import com.example.systembank.model.accounts.Account;
import lombok.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferAccToAccDTO {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Account receiverA;
}

