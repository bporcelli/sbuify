package com.cse308.sbuify.customer;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Customer extends User implements Followable {

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
	private Playlist library;

	// Profile image for customer. When customer is updated/deleted, cascade.
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Image profileImage;

	/** Customers that follow this customer. */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "follower_id"))
    @JsonIgnore
	private Set<Customer> followers = new HashSet<>();

	/** Playlists followed by this customer. */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    @JsonIgnore
    private Set<Playlist> playlists = new HashSet<>();

	/** Customers followed by this customer. */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @JsonIgnore
    private Set<Customer> friends = new HashSet<>();

    /** Artists followed by this customer. */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @JsonIgnore
    private Set<Artist> artists = new HashSet<>();

	public Customer() {
	}

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
		this.setLibrary(new Playlist(getName(), this, null, true, 0));
		this.setPlayQueue(new PlayQueue());
	}

    /**
     * Followable methods.
     */
    @Override
    public void addFollower(Customer customer) {
        this.followers.add(customer);
    }

    @Override
    public void removeFollower(Customer customer) {
        this.followers.remove(customer);
    }

    @Override
    public Boolean isFollowedBy(Customer customer) {
        return this.followers.contains(customer);
    }

    /**
     * Does the customer follow the given followable?
     *
     * @param followable
     * @return boolean indicating whether the customer follows the followable.
     */
    public Boolean isFollowing(Followable followable) {
        if (followable instanceof Playlist) {
            return this.playlists.contains(followable);
        } else if (followable instanceof Customer) {
            return this.friends.contains(followable);
        } else if (followable instanceof Artist) {
            return this.artists.contains(followable);
        }
        return false;
    }

    /**
     * Follow a playlist or customer.
     *
     * @param followable
     */
    public void follow(Followable followable) {
        followable.addFollower(this);

        if (followable instanceof Customer) {
            this.friends.add((Customer) followable);
        } else if (followable instanceof Playlist) {
            this.playlists.add((Playlist) followable);
        } else if (followable instanceof Artist) {
        	this.artists.add((Artist) followable);
		}
    }

    /**
     * Unfollow a playlist or customer.
     *
     * @param followable
     */
    public void unfollow(Followable followable) {
        followable.removeFollower(this);

        if (followable instanceof Customer) {
            this.friends.remove(followable);
        } else if (followable instanceof Playlist) {
            this.playlists.remove(followable);
        } else if (followable instanceof Artist) {
            this.artists.remove(followable);
        }
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

	public Playlist getLibrary() {
		return library;
	}

	public void setLibrary(Playlist library) {
		this.library = library;
	}

	public Image getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(Image profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return AUTHORITIES;
	}

	@Override
	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	@JsonIgnore
	public boolean isPremium() {
		return subscription != null;
	}

    public Set<Customer> getFollowers() {
        return followers;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public Set<Customer> getFriends() {
        return friends;
    }

    public Set<Artist> getArtists() {
        return artists;
    }
}
