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

    public Integer getTop_50_by_genre() {
        return top_50_by_genre;
    }

    public void setTop_50_by_genre(Integer top_50_by_genre) {
        this.top_50_by_genre = top_50_by_genre;
    }

    public Integer getTop_50_by_artist() {
        return top_50_by_artist;
    }

    public void setTop_50_by_artist(Integer top_50_by_artist) {
        this.top_50_by_artist = top_50_by_artist;
    }

    public Integer getMonthly_artist_duo() {
        return monthly_artist_duo;
    }

    public void setMonthly_artist_duo(Integer monthly_artist_duo) {
        this.monthly_artist_duo = monthly_artist_duo;
    }

    public Integer getMonthly_artist_triplet() {
        return monthly_artist_triplet;
    }

    public void setMonthly_artist_triplet(Integer monthly_artist_triplet) {
        this.monthly_artist_triplet = monthly_artist_triplet;
    }

    public Integer getMonthly_genre_duo() {
        return monthly_genre_duo;
    }

    public void setMonthly_genre_duo(Integer monthly_genre_duo) {
        this.monthly_genre_duo = monthly_genre_duo;
    }

    public Integer getMonthly_genre_triplet() {
        return monthly_genre_triplet;
    }

    public void setMonthly_genre_triplet(Integer monthly_genre_triplet) {
        this.monthly_genre_triplet = monthly_genre_triplet;
    }

    public Integer getSbu_community() {
        return sbu_community;
    }

    public void setSbu_community(Integer sbu_community) {
        this.sbu_community = sbu_community;
    }

    public Integer getPlaylist_mix_n_match() {
        return playlist_mix_n_match;
    }

    public void setPlaylist_mix_n_match(Integer playlist_mix_n_match) {
        this.playlist_mix_n_match = playlist_mix_n_match;
    }
}
