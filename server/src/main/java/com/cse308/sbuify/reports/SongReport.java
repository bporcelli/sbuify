package com.cse308.sbuify.reports;

public class SongReport {

    /** Number of free streams in period */
    private Long freeStreams;

    /** Number of premium streams in period */
    private Long premiumStreams;

    public SongReport(Long freeStreams, Long premiumStreams) {
        this.freeStreams = freeStreams;
        this.premiumStreams = premiumStreams;
    }

    public Long getFreeStreams() {
        return freeStreams;
    }

    public void setFreeStreams(Long freeStreams) {
        this.freeStreams = freeStreams;
    }

    public Long getPremiumStreams() {
        return premiumStreams;
    }

    public void setPremiumStreams(Long premiumStreams) {
        this.premiumStreams = premiumStreams;
    }

    public Long getTotalStreams() {
        return this.freeStreams + this.premiumStreams;
    }
}
