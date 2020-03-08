package com.projectdummy.dummy.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement
@Entity
@Getter
@Setter
@ToString
@Component
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String customerCode;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Column
    private String address;
    @Column (nullable = false)
    private long balance;
    @Column
    private String gender;
    @Column
    private String email;
    @Column
    private String dob;
    @Column
    private String mothersMaidenName;
    @Column
    private String accountNumber;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Fingerprint> fingerprints = new ArrayList<>();

    public Customer() {
    }


    public Customer(String customerCode, String firstName, String lastName, String phoneNumber, String address, long balance, String gender, String email, String dob, String mothersMaidenName, String accountNumber, List<Fingerprint> fingerprints) {
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.balance = balance;
        this.gender = gender;
        this.email = email;
        this.dob = dob;
        this.mothersMaidenName = mothersMaidenName;
        this.accountNumber = accountNumber;
        this.fingerprints = fingerprints;
    }


    public void addFingerprint(byte[] print, String fingertype){
       Fingerprint fingerprint =  new Fingerprint();
       fingerprint.setPrintObject(print);
       fingerprint.setFingerType(fingertype);

       fingerprints.add(fingerprint);
    }

    public void addFingerprint(byte[] print) {
        addFingerprint(print, null);

    }
}
