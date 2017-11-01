package com.cse308.sbuify.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity // This tells Hibernate to make a table out of this class
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public abstract  class User implements Serializable{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    @NotNull
    private String email;
    @NotNull
    private String hashPassword;
    private String token;
    private Image profileImage;

    // TODO: add CustomerSubscription relationship

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
