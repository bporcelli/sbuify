package com.cse308.sbuify.concert;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Concert extends  CatalogItem{

    @OneToMany
    private Set<Artist> lineUp= new LinkedHashSet<>();

    @NotNull
    private LocalDateTime time;

    @OneToMany
    private List<Venue> venue = new ArrayList<>();

    public Concert() {
    }

    public Concert(Set<Artist> lineUp, @NotNull LocalDateTime time, List<Venue> venue) {
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

    public List<Venue> getVenue() {
        return venue;
    }

    public void setVenue(List<Venue> venue) {
        this.venue = venue;
    }

}
