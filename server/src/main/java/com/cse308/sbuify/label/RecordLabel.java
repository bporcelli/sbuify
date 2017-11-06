package com.cse308.sbuify.label;


import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a Record Label.
 */
@Entity
public class RecordLabel extends User {

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

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_LABEL";
    }
}
