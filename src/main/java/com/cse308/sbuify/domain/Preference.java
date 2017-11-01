package com.cse308.sbuify.domain;



import javax.persistence.*;

import com.cse308.sbuify.enums.Language;
import com.cse308.sbuify.enums.RepeatMode;

import java.io.Serializable;

@Entity
public class Preference implements Serializable{
    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Language langauge;
    private Boolean hdStreaming;
    private Boolean privateSession;
    private Boolean showActivityFeed;
    private Boolean shuffle;
    @Enumerated(EnumType.STRING)
    private RepeatMode repeat;
    private Customer customer;

}
