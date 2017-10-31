package com.sbuify.SBUify.domain;

import javax.persistence.Entity;

@Entity
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String purchaseURL;
    private Artist artist;
}
