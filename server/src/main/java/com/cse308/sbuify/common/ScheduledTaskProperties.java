package com.cse308.sbuify.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

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

    /**
     * Payment owed to labels for each free stream.
     */
    private BigDecimal costPerFreeStream;

    /**
     * Payment owed to labels for each premium stream.
     */
    private BigDecimal costPerPremiumStream;

    public Integer getNumMixAndMatch() {
        return numMixAndMatch;
    }

    public Integer getPlayCountUpdateInterval() {
        return playCountUpdateInterval;
    }

    public Integer getMonthlyListenersUpdateInterval() {
        return monthlyListenersUpdateInterval;
    }

    public BigDecimal getCostPerFreeStream() {
        return costPerFreeStream;
    }

    public void setCostPerFreeStream(BigDecimal costPerFreeStream) {
        this.costPerFreeStream = costPerFreeStream;
    }

    public BigDecimal getCostPerPremiumStream() {
        return costPerPremiumStream;
    }

    public void setCostPerPremiumStream(BigDecimal costPerPremiumStream) {
        this.costPerPremiumStream = costPerPremiumStream;
    }
}
