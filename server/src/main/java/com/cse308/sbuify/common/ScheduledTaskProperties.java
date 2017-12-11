package com.cse308.sbuify.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("scheduler")
public class ScheduledTaskProperties {

    /**
     * Number of playlists to include in the "Mix and Match" list.
     */
    private Integer numMixAndMatch = 25;

    /**
     * Number of hours between play count updates.
     */
    private Integer playCountUpdateInterval = 6;

    /**
     * Number of days between monthly listener count updates.
     */
    private Integer monthlyListenersUpdateInterval = 30;

    public Integer getNumMixAndMatch() {
        return numMixAndMatch;
    }

    public Integer getPlayCountUpdateInterval() {
        return playCountUpdateInterval;
    }

    public Integer getMonthlyListenersUpdateInterval() {
        return monthlyListenersUpdateInterval;
    }
}
