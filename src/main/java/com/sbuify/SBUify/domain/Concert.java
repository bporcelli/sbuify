package com.sbuify.SBUify.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Concert extends  CatalogItem implements Serializable{
    @ElementCollection(targetClass= Artist.class)
    private Set<Artist> lineUp= new LinkedHashSet<Artist>();
    private LocalDateTime time;
    private Venue venue;

}
