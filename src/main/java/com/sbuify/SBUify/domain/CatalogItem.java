package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class CatalogItem {
    private Integer id;
    private String name;
    private LocalDateTime dateCreation;
    private Boolean isActive;
    private User Owner;
}
