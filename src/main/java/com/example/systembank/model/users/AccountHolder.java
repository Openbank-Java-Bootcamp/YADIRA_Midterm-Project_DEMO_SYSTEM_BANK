package com.example.systembank.model.users;

import com.example.systembank.model.accounts.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue(value="ACCOUNT_HOLDER")
public class AccountHolder extends User{


    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Embedded
    private Address primaryAddress;

     @AttributeOverrides({
          @AttributeOverride(name = "streetAddress", column = @Column(name = "mailing_street")),
          @AttributeOverride(name = "zipCode", column = @Column(name = "mailing_zip_code")),
          @AttributeOverride(name = "city", column = @Column(name = "mailing_city"))
    })
    @Embedded
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<Account> accountPrimaryOwnerList;

    @OneToMany(mappedBy = "secondaryOwner", fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<Account> accountSecondaryOwnerList;

    public AccountHolder(String name, String username, String password, Date dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public AccountHolder(String name, String username, String password, Role role, Date dateOfBirth, Address primaryAddress, Address mailingAddress) {
        super(name, username, password, role);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountHolder that = (AccountHolder) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

