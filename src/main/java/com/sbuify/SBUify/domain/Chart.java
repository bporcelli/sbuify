package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
public class Chart extends PlayList {
    private Date date;
    private Chart previousChart;


}
