package com.cse308.sbuify.reports;

public class SubscribersReport {

    /** Subs gained over the reporting period */
    private Integer subscribersGained;

    /** Subs lost over the reporting period */
    private Integer subscribersLost;

    /** Subscribers at end of period */
    private Integer totalSubscribers;

    public SubscribersReport(Integer subscribersGained, Integer subscribersLost, Integer totalSubscribers) {
        this.subscribersGained = subscribersGained;
        this.subscribersLost = subscribersLost;
        this.totalSubscribers = totalSubscribers;
    }

    public Integer getSubscribersGained() {
        return subscribersGained;
    }

    public void setSubscribersGained(Integer subscribersGained) {
        this.subscribersGained = subscribersGained;
    }

    public Integer getSubscribersLost() {
        return subscribersLost;
    }

    public void setSubscribersLost(Integer subscribersLost) {
        this.subscribersLost = subscribersLost;
    }

    public Integer getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(Integer totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }
}
