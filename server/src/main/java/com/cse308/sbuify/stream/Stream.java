package com.cse308.sbuify.stream;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Stream implements Serializable {

    // todo: make sure stream is kept around when the playlist and/or customer are deleted.

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Boolean premium;

    @NotNull
    private LocalDateTime time;

    @OneToOne
    private Playlist playlist;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "stream")
    private List<TimeRange> played;

    @OneToOne
    @JsonIgnoreProperties("password")
    private Customer customer;

    @NotNull
    @OneToOne
    private Song song;

    public Stream() {}

    public Stream(@NotNull Boolean premium,
                  @NotNull LocalDateTime time,
                  List<TimeRange> played,
                  Customer customer,
                  Song song) {
        this.premium = premium;
        this.time = time;
        this.played = played;
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

    public List<TimeRange> getPlayed() {
        return played;
    }

    public void setPlayed(List<TimeRange> played) {
        this.played = played;
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
