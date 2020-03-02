package com.projectdummy.dummy.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Entity
@Getter
@Setter
@ToString
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

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Fingerprint> fingerprints = new ArrayList<>();

    public Customer() {
    }

    public Customer(Long id, String customerCode, String firstName, String lastName, String phoneNumber, String address, List<Fingerprint> fingerprints) {
        this.id = id;
        this.customerCode = customerCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
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
