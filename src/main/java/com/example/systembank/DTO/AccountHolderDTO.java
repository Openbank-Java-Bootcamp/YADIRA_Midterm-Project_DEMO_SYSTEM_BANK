package com.example.systembank.DTO;

import com.example.systembank.model.users.Address;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountHolderDTO {

    @Pattern(regexp = "^[a-zA-Z\\s]{2,254}")
    @NotEmpty(message = "The name cannot be empty")
    private String name;

    @NotEmpty(message = "The username cannot be empty")
    private String username;

    @NotEmpty(message = "The password cannot be empty")
    private String password;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")
    private String dateOfBirth;

    @NotNull
    private Address primaryAddress;

    private Address mailingAddress;

    public AccountHolderDTO(String name, String username, String password, String dateOfBirth, Address primaryAddress) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
    }
}
