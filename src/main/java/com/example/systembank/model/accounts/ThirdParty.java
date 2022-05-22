package com.example.systembank.model.accounts;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.example.systembank.model.transactions.Transfer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ThirdParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String hashedKey;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "senderTP")
    @ToString.Exclude
    @JsonIgnore
    protected List<Transfer> sendList;

    @OneToMany(mappedBy = "receiverTP")
    @ToString.Exclude
    @JsonIgnore
    protected List<Transfer> receiveList;

    public ThirdParty(String hashedKey, String name) {
        this.hashedKey = hashedKey;
        this.name = name;
    }

}
