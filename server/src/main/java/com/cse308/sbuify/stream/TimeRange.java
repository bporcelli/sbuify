package com.cse308.sbuify.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "stream_time_ranges")
public class TimeRange {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Double start;

    @NotNull
    private Double end;

    @NotNull
    @ManyToOne
    @JsonIgnore
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

