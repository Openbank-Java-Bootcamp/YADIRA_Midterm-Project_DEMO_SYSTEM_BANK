package com.example.systembank.model.accounts;

import com.example.systembank.enums.Status;
import com.example.systembank.model.transactions.Transfer;
import com.example.systembank.model.users.AccountHolder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorColumn(name="type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Embedded
    protected Money balance;

    @ManyToOne
    @JoinColumn(name = "primary_owner_id", referencedColumnName = "id", nullable = false)
    protected AccountHolder primaryOwner;

    @ManyToOne
    @JoinColumn(name = "secondary_owner_id", referencedColumnName = "id")
    protected AccountHolder secondaryOwner;

    protected String secretKey;

    @Enumerated(EnumType.STRING)
    protected Status status = Status.ACTIVE;

    @Temporal(TemporalType.DATE)
    protected Date creationDate;

    @Temporal(TemporalType.DATE)
    private Date lastConsultation = Calendar.getInstance().getTime();

    protected BigDecimal penaltyFee = new BigDecimal("40");

    @OneToMany(mappedBy = "senderA")
    @ToString.Exclude
    @JsonIgnore
    protected List<Transfer> sendList;

    @OneToMany(mappedBy = "receiverA")
    @ToString.Exclude
    @JsonIgnore
    protected List<Transfer> receiveList;

    public Account(Money balance, AccountHolder primaryOwner, AccountHolder secondaryOwner, String secretKey) {
        this.balance = balance;
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.secretKey = secretKey;
        setDate();
    }

    public void setDate(){
        LocalDate localDate = LocalDate.now();
        this.creationDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

