package com.sbuify.SBUify.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity

public class Biography implements Serializable {
    @Id
    private Integer id;
    private String bio;
    @ElementCollection(targetClass=Image.class)
    private List<Image> images;


}
