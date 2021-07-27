package com.library.librarysystem.model;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
