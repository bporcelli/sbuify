package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

@Entity
public class Concert extends  CatalogItem{
    private LinkedHashSet<Artist> lineUp;
    private LocalDateTime time;
    private Venue venue;

}
