package com.cse308.sbuify.label;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.user.User;

/**
 * Entity representing a Record Label.
 */
@Entity
public class RecordLabel extends User {

    // Authorities granted to customers
    private final static Collection<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_LABEL"));
    }

    /**
     * The MusicBrainz label this RecordLabel is associated with.
     */
    @OneToOne
    @NotNull
    private Label label;

    /**
     * Artists managed by the record label.
     */
    @OneToMany(mappedBy = "owner")
    private Set<Artist> artists = new HashSet<>();

    public RecordLabel() {
    }

    public RecordLabel(@NotNull String email, @NotNull String password, @NotNull Label label) {
        super(email, password);
        this.label = label;
    }

    public String getName() {
        return label.getName();
    }

    public void setName(String name) {
        label.setName(name);
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }
}
