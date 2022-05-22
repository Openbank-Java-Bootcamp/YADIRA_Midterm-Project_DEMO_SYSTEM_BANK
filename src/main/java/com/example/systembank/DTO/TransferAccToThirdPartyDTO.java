package com.example.systembank.DTO;

import lombok.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransferAccToThirdPartyDTO {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String hashedKey;
}
