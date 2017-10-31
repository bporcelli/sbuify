package com.sbuify.SBUify.domain;

import javax.persistence.Entity;

@Entity
public class TimeRange {
    private Integer id;
    private Double start;
    private Double end;
    public Stream stream;
}

