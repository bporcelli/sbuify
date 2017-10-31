package com.sbuify.SBUify.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "admin")
public class Admin extends User implements Serializable{
    private String firstName;
    private String lastName;
    private Boolean isSuperAdmin;

}
