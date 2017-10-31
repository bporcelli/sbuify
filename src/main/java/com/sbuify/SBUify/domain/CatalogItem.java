package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class CatalogItem implements Serializable {
    @Id
    private Integer id;
    private String name;
    private LocalDateTime dateCreation;
    private Boolean isActive;
    private User Owner;
    private Image image;
}
