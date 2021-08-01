package com.library.librarysystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MemberAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToOne
    private Person person;

    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date dateOfCreation;

    public MemberAccount(AccountStatus accountStatus, Person person, Date dateOfCreation) {
        this.accountStatus = accountStatus;
        this.person = person;
        this.dateOfCreation = dateOfCreation;
    }

    public void addToBlacklist(){
        this.accountStatus = AccountStatus.BLACKLISTED;
    }

    public void blockAccount(){
        this.accountStatus = AccountStatus.BLOCKED;
    }

    public void changeAddress(Address address){
        this.person.setAddress(address);
    }

    public void changeEmail(String email){
        this.person.setEmail(email);
    }
}
