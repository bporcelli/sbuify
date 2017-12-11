package com.cse308.sbuify.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("scheduler")
public class ScheduleTasksProperties {

    private Integer top_50_by_genre = 3715;
    private Integer top_50_by_artist = 3716;
    private Integer monthly_artist_duo = 3717;
    private Integer monthly_artist_triplet = 3718;
    private Integer monthly_genre_duo = 3719;
    private Integer monthly_genre_triplet = 3720;
    private Integer sbu_community = 3721;
    private Integer playlist_mix_n_match = 25;
    private Integer every_nth_hour = 6;
    private Integer every_nth_day = 30;

    public Integer getTop_50_by_genre() {
        return top_50_by_genre;
    }

    public Integer getTop_50_by_artist() {
        return top_50_by_artist;
    }

    public Integer getMonthly_artist_duo() {
        return monthly_artist_duo;
    }

    public Integer getMonthly_artist_triplet() {
        return monthly_artist_triplet;
    }

    public Integer getMonthly_genre_duo() {
        return monthly_genre_duo;
    }

    public Integer getMonthly_genre_triplet() {
        return monthly_genre_triplet;
    }

    public Integer getSbu_community() {
        return sbu_community;
    }

    public Integer getPlaylist_mix_n_match() {
        return playlist_mix_n_match;
    }

    public Integer getEvery_nth_hour() {
        return every_nth_hour;
    }

    public Integer getEvery_nth_day() {
        return every_nth_day;
    }
}
