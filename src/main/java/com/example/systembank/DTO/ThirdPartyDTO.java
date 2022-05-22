package com.example.systembank.DTO;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ThirdPartyDTO {

    @NotNull
    private String name;
}
