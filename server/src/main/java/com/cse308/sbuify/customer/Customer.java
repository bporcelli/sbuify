package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.common.api.Decorable;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Indexed
public class Customer extends User implements Followable, Decorable {

	/** Authorities granted to customers */
	private final static Collection<GrantedAuthority> AUTHORITIES = Arrays.asList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

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
	private Playlist library;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Image profileImage;

	@Transient
	private Map<String, Object> properties = new HashMap<>();

	public Customer() {
	}

	public Customer(@NotNull String email, @NotNull String password, String firstName, String lastName, Date birthday) {
		super(email, password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
	}

	/**
	 * Initialize the customer just before they are saved to the database.
	 */
	@PrePersist
	private void initialize() {
		this.setLibrary(new Playlist(getName(), this, null, true));
		this.setPlayQueue(new PlayQueue());
	}

    /**
	 * The customer's first name.
	 */
	public String getFirstName() {
		return firstName;
	}

    /**
     * {@link #getFirstName()}
     */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

    /**
     * The customer's last name.
     */
	public String getLastName() {
		return lastName;
	}

    /**
     * {@link #getLastName()}
     */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

    /**
     * The customer's birthday.
     */
	public Date getBirthday() {
		return birthday;
	}

    /**
     * {@link #getBirthday()
     */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

    /**
     * The customer's subscription, or null if the customer is not a subscriber.
     */
	public Subscription getSubscription() {
		return subscription;
	}

    /**
     * {@link #getSubscription()}
     */
	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

    /**
     * The customer's play queue.
     */
	public PlayQueue getPlayQueue() {
		return playQueue;
	}

    /**
     * {@link #getPlayQueue()}
     */
	public void setPlayQueue(PlayQueue playQueue) {
		this.playQueue = playQueue;
	}

    /**
     * The customer's library of saved music.
     */
	public Playlist getLibrary() {
		return library;
	}

    /**
     * {@link #getLibrary()}
     */
	public void setLibrary(Playlist library) {
		this.library = library;
	}

    /**
     * The customer's profile image, or null if they haven't set one.
     */
	public Image getProfileImage() {
		return profileImage;
	}

    /**
     * {@link #getProfileImage()}
     */
	public void setProfileImage(Image profileImage) {
		this.profileImage = profileImage;
	}

    /**
     * A boolean flag indicating whether the customer is a premium user (subscriber).
     */
    public boolean isPremium() {
        return subscription != null;
    }

    /**
     * Get the transient properties of this customer.
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Get a specific transient property of this customer.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * Set a transient property of this customer.
     */
    @JsonAnySetter
    public void set(String key, Object value) {
        properties.put(key, value);
    }

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AUTHORITIES;
	}

	@Override
    @JsonIgnore(false)
    @Field(name = "name")
	public String getName() {
		return this.firstName + " " + this.lastName;
	}
}
