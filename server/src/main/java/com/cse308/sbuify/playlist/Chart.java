package com.cse308.sbuify.playlist;


import javax.persistence.Entity;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Chart extends PlayList implements Serializable {
    private Date date;
    private Chart previousChart;
}
