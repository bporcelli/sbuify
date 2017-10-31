package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity

public class Biography {
    private Integer id;
    private String bio;
    private List<Image> images;


}
