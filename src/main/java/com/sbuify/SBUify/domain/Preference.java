package com.sbuify.SBUify.domain;

import com.sun.tools.internal.xjc.Language;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
public class Preference {
    private Language langauge;
    private Boolean hdStreaming;
    private Boolean privateSession;
    private Boolean showActivityFeed;
    private Boolean shuffle;
    @Enumerated(EnumType.STRING)
    private RepeatMode repeat;

}
