package com.projectdummy.dummy.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
public class Fingerprint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fingerType;
    private byte[] printObject;
}
