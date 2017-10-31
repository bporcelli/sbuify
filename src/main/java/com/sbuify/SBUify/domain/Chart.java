package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Chart extends PlayList implements Serializable {
    private Date date;
    private Chart previousChart;
}
