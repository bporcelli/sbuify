package com.cse308.sbuify.common;

import com.cse308.sbuify.stream.Stream;

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

