package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
public class Song {
    private Integer id;
    private Integer trackNumber;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    // TODO: featured artist
}
