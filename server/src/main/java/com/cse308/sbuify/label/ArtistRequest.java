package com.cse308.sbuify.label;

import com.cse308.sbuify.artist.Artist;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ArtistRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    private Artist artist;

    @ManyToOne
    @NotNull
    private RecordLabel label;

    @NotNull
    private LocalDateTime created;

    public ArtistRequest() {}

    public ArtistRequest(@NotNull RecordLabel label, @NotNull Artist artist) {
        this.label = label;
        this.artist = artist;
    }

    /**
     * Set created date when artist request is persisted.
     */
    @PrePersist
    private void onPrePersist() {
        this.created = LocalDateTime.now();
    }

    /**
     * Getters and setters.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    private void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public RecordLabel getLabel() {
        return label;
    }

    public void setLabel(RecordLabel label) {
        this.label = label;
    }
}
