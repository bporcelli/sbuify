package com.cse308.sbuify.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @Id
    private Integer id;
    private String name;
    private String description;
    private String purchaseURL;
    private Artist artist;
}
