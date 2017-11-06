package com.cse308.sbuify.customer;

import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.Library;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Customer extends User {

    // Default preferences
    // todo: better way to handle this?
    private final static Map<String, String> DEFAULT_PREFS = new HashMap<>();

    static {
        DEFAULT_PREFS.put(Preferences.HQ_STREAMING, "false");
        DEFAULT_PREFS.put(Preferences.LANGUAGE, Language.ENGLISH.name());
    }

    // Authorities granted to customers
    private final static Collection<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    }

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    private Date birthday;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Subscription subscription;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @JsonIgnore
    private PlayQueue playQueue;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    @JsonIgnore
    private Library library;

    @ElementCollection
    @MapKeyColumn(name = "PREF_KEY")
    @Column(name = "PREF_VALUE")
    @CollectionTable(name = "customer_preferences")
    private Map<String, String> preferences = DEFAULT_PREFS;

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

    /**
     * Initialize the customer just after they are saved to the database.
     */
    @PrePersist
    private void initialize() {
        // Create library
        Library library = new Library(this);
        this.setLibrary(library);

        // Create Play Queue
        PlayQueue playQueue = new PlayQueue();
        this.setPlayQueue(playQueue);
    }

    /**
     * Getters and setters.
     */
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

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }
}
