package com.cse308.sbuify.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TimeRange {
    @Id
    private Integer id;
    private Double start;
    private Double end;
    public Stream stream;
}

