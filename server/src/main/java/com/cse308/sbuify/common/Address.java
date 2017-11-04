package com.cse308.sbuify.common;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
public class Address implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    private String addressLine1;

    @NotEmpty
    private String addressline2;

    @NotEmpty
    private String city;

    @NotEmpty
    private String state;

    @NotEmpty
    private String country;

    @NotEmpty
    private String zipcode;

    public Address() {
    }

    public Address(Integer id, @NotEmpty String addressLine1, @NotEmpty String addressline2, @NotEmpty String city, @NotEmpty String state, @NotEmpty String country, @NotEmpty String zipcode) {
        this.id = id;
        this.addressLine1 = addressLine1;
        this.addressline2 = addressline2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public String getState() {

        return state;
    }

    public void setState(String state) {

        this.state = state;
    }

    public String getCountry() {

        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public String getZipcode() {

        return zipcode;
    }

    public void setZipcode(String zipcode) {

        this.zipcode = zipcode;
    }
}
