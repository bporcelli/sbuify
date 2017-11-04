package com.cse308.sbuify.label;

import com.cse308.sbuify.common.CatalogItem;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Label extends CatalogItem {

    @Column(unique = true)
    private String musicBrainzId;
}
