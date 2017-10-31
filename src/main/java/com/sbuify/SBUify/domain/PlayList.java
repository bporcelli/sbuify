package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class PlayList {
    private Integer id;
    private String description;
    private Boolean isPrivate;
    private Integer numSongs;
    // TODO: saved songs and catalog item
}
