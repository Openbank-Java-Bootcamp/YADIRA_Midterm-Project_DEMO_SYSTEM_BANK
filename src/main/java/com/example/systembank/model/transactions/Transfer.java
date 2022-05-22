package com.example.systembank.model.transactions;

import com.example.systembank.model.accounts.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Money amount;

    @DateTimeFormat
    private Date date;

    @ManyToOne
    @JoinColumn(name="sender_account_id")
    private Account senderA;

    @ManyToOne
    @JoinColumn(name="receiver_account_id")
    private Account receiverA;

    @ManyToOne
    @JoinColumn(name="sender_third_party_id")
    private ThirdParty senderTP;

    @ManyToOne
    @JoinColumn(name="receiver_third_party_id")
    private ThirdParty receiverTP;

    public Transfer(Money amount, Account senderA, Account receiverA) {
        this.amount = amount;
        this.senderA = senderA;
        this.receiverA = receiverA;
        this.date = Calendar.getInstance().getTime();
    }

    public Transfer(Money amount, Account senderA, Account receiverA, ThirdParty senderTP, ThirdParty receiverTP) {
        this.amount = amount;
        this.senderA = senderA;
        this.receiverA = receiverA;
        this.senderTP = senderTP;
        this.receiverTP = receiverTP;
        this.date = Calendar.getInstance().getTime();
    }

    public Transfer(Money amount, Date date, Account senderA, Account receiverA) {
        this.amount = amount;
        this.date = date;
        this.senderA = senderA;
        this.receiverA = receiverA;
    }

    public Transfer(Money amount, Date date, Account senderA, Account receiverA, ThirdParty senderTP, ThirdParty receiverTP) {
        this.amount = amount;
        this.date = date;
        this.senderA = senderA;
        this.receiverA = receiverA;
        this.senderTP = senderTP;
        this.receiverTP = receiverTP;
    }
}
