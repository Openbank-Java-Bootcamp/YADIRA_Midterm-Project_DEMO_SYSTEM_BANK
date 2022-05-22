package com.example.systembank.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferThirdPartyToAccDTO {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long idAcc;

    @NotNull
    private String secretKeyAcc;

}
