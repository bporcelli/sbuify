package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
public class PlayQueue implements Serializable{
    @Id
    private Integer id;

}
