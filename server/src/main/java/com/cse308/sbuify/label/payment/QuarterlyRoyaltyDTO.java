package com.cse308.sbuify.label.payment;

import com.cse308.sbuify.label.Label;

public class QuarterlyRoyaltyDTO {
    private Label label;
    private Long premiumStreams;
    private Long freeStreams;

    public QuarterlyRoyaltyDTO(Label label, Long premiumStreams, Long freeStreams) {
        this.label = label;
        this.premiumStreams = premiumStreams;
        this.freeStreams = freeStreams;
    }

    public Label getLabel() {
        return label;
    }

    public Long getPremiumStreams() {
        return premiumStreams;
    }

    public Long getFreeStreams() {
        return freeStreams;
    }
}
