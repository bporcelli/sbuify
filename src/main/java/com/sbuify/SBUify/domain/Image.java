package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Image implements Serializable{
    @Id
    private Integer id;
    private String path;
    @Enumerated(EnumType.STRING)
    private ImageSize size;

}
