package com.cse308.sbuify.label;

import com.cse308.sbuify.artist.Artist;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ArtistRequest implements Serializable{
    @Id
    private Integer id;

    @NotNull
    private LocalDateTime creation;

    @OneToMany
    @Column(unique = true)
    private Set<RecordLabel> requestTo = new HashSet<>();

    @OneToOne
    private Artist requestFrom;

    public ArtistRequest() {
    }

    public ArtistRequest(@NotNull LocalDateTime creation, Set<RecordLabel> requestTo, Artist requestFrom) {
        this.creation = creation;
        this.requestTo = requestTo;
        this.requestFrom = requestFrom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public Set<RecordLabel> getRequestTo() {
        return requestTo;
    }

    public void setRequestTo(Set<RecordLabel> requestTo) {
        this.requestTo = requestTo;
    }

    public Artist getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(Artist requestFrom) {
        this.requestFrom = requestFrom;
    }
}
