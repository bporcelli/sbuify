package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Image {
    private Integer id;
    private String path;
    @Enumerated(EnumType.STRING)
    private ImageSize size;

}
