package com.cse308.sbuify.customer;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.playlist.Library;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Customer extends User {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    private Date birthday;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Subscription subscription;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @NotNull
    private PlayQueue playQueue;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @NotNull
    private Library library;

    @ElementCollection
    @MapKeyColumn(name = "PREF_KEY")
    @Column(name = "PREF_VALUE")
    @CollectionTable(name = "customer_preferences")
    private Map<String, String> preferences = new HashMap<>();

    // Profile image for customer. When customer is updated/deleted, cascade.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image profileImage;

    // Must include no-arg constructor to satisfy Jackson
    public Customer() {}

    public Customer(@NotNull String email, @NotNull String password, String firstName, String lastName, Date birthday) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public PlayQueue getPlayQueue() {
        return playQueue;
    }

    public void setPlayQueue(PlayQueue playQueue) {
        this.playQueue = playQueue;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

    public Map<String, String> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, String> preferences) {
        this.preferences = preferences;
    }

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_CUSTOMER";
    }
}
