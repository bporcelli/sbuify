package com.cse308.sbuify.common;

import com.cse308.sbuify.stream.Stream;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class TimeRange {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private Double start;

    @NotNull
    private Double end;

    @ManyToOne
    public Stream stream;

    public TimeRange() {}

    public TimeRange(@NotNull Double start, @NotNull Double end, Stream stream) {
        this.start = start;
        this.end = end;
        this.stream = stream;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    public Double getEnd() {
        return end;
    }

    public void setEnd(Double end) {
        this.end = end;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}

