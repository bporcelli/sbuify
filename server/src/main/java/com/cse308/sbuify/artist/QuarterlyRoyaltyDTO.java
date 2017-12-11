package com.cse308.sbuify.artist;

import java.math.BigDecimal;

public class QuarterlyRoyaltyDTO {
    private Artist artist;
    private BigDecimal payment;
    public static final double STREAM_COST = .05;

    public QuarterlyRoyaltyDTO(Artist artist, Long streams) {
        this.artist = artist;
        this.payment = new BigDecimal(streams * STREAM_COST);
    }

    public Artist getArtist() {
        return artist;
    }

    public BigDecimal getPayment() {
        return payment;
    }
}
