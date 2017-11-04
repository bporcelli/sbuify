package com.cse308.sbuify.stream;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.common.TimeRange;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Stream implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private  Boolean premium;

    @NotNull
    private LocalDateTime time;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Playlist playlist;

    @OneToMany
    private List<TimeRange> timeRanges;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Customer customer;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Song song;

    public Stream() {
    }

    public Stream(@NotNull Boolean premium, @NotNull LocalDateTime time, Playlist playlist, List<TimeRange> timeRanges, Customer customer, Song song) {
        this.premium = premium;
        this.time = time;
        this.playlist = playlist;
        this.timeRanges = timeRanges;
        this.customer = customer;
        this.song = song;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public List<TimeRange> getTimeRanges() {
        return timeRanges;
    }

    public void setTimeRanges(List<TimeRange> timeRanges) {
        this.timeRanges = timeRanges;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }
}
