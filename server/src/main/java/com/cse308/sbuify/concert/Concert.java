package com.cse308.sbuify.concert;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Concert extends CatalogItem {

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "artist_id"))
    private Set<Artist> lineUp = new LinkedHashSet<>();

    @NotNull
    private LocalDateTime time;

    @OneToOne
    private Venue venue;

    public Concert() {}

    public Concert(Set<Artist> lineUp, @NotNull LocalDateTime time, Venue venue) {
        this.lineUp = lineUp;
        this.time = time;
        this.venue = venue;
    }

    public Set<Artist> getLineUp() {
        return lineUp;
    }

    public void setLineUp(Set<Artist> lineUp) {
        this.lineUp = lineUp;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
