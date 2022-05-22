package com.example.systembank.model.users;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorColumn(name="role_name")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull(message = "Name cannot be null")
    protected String name;

    @NotNull(message = "Username cannot be null")
    protected String username;

    @NotNull(message = "Password cannot be null")
    protected String password;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    protected Role role;

    public User(String name, String username, String password, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
