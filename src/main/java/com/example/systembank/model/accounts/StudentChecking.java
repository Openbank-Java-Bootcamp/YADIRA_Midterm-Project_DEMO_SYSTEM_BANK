package com.example.systembank.model.accounts;

import com.example.systembank.model.users.AccountHolder;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@DiscriminatorValue(value="student_checking")
public class StudentChecking extends Account {

    public StudentChecking(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        super(balance, primaryOwner, secondaryOwner, secretKey);
        this.setPenaltyFee(new BigDecimal("0"));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StudentChecking that = (StudentChecking) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}