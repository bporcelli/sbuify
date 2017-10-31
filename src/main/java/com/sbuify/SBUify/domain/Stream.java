package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Stream {
    private Integer id;
    private  Boolean isPremium;
    private LocalDateTime time;
    private PlayList playList;
    private List<TimeRange> timeRanges;
    private Customer customer;
    private Song song;
    private PlayList playlist;
}
