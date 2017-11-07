package com.cse308.sbuify.label;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.artist.Artist;

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
