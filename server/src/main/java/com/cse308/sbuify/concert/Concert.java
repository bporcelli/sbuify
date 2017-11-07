package com.cse308.sbuify.concert;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;

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
