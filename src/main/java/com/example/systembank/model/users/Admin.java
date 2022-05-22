package com.example.systembank.model.users;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@DiscriminatorValue(value="ADMIN")
public class Admin extends User {

    public Admin(String name, String username, String password, Role role) {
        super(name, username, password, role);
    }

    public Admin(Long id, String name, String username, String password, Role role) {
        super(id, name, username, password, role);
    }
}




