package com.cse308.sbuify.user;

import com.cse308.sbuify.domain.Image;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * Generic entity representing an application user.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Customer.class, name = "customer"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin")
})
public abstract class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotNull
	private String email;

    // Hashed password
	@NotNull
	private String password;

    // Token used for password reset requests
	private String token;

	// Profile image for customer. When customer is updated/deleted, cascade.
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Image profileImage;

	// Must include no-arg constructor to satisfy Jackson
	public User() {}

    public User(@NotNull String email, @NotNull String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * Return the role name that corresponds to this User instance, e.g. "ROLE_CUSTOMER."
     */
    @JsonIgnore
    public abstract String getRole();

}
