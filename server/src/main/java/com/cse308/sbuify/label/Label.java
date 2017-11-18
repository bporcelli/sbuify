package com.cse308.sbuify.label;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.cse308.sbuify.common.CatalogItem;

@Entity
public class Label extends CatalogItem {

    @Column(unique = true)
    private String MBID;
}
