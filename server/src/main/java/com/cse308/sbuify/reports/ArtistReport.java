package com.cse308.sbuify.reports;

import com.cse308.sbuify.album.AlbumStreamCountDTO;
import com.cse308.sbuify.stream.StreamCountDTO;

import java.util.List;

public class ArtistReport {

    /** Free streams of the artist's songs over the reporting period. */
    private Long freeStreams;

    /** Premium streams of the artist's songs over the reporting period. */
    private Long premiumStreams;

    /** Top 10 songs over the reporting period. */
    private List<StreamCountDTO> popularSongs;

    /** Top 10 albums over the reporting period. */
    private List<AlbumStreamCountDTO> popularAlbums;

    public ArtistReport(
        Long freeStreams, Long premiumStreams,
        List<StreamCountDTO> popularSongs,
        List<AlbumStreamCountDTO> popularAlbums
    ) {
        this.freeStreams = freeStreams;
        this.premiumStreams = premiumStreams;
        this.popularSongs = popularSongs;
        this.popularAlbums = popularAlbums;
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
        return freeStreams + premiumStreams;
    }

    public List<StreamCountDTO> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<StreamCountDTO> popularSongs) {
        this.popularSongs = popularSongs;
    }

    public List<AlbumStreamCountDTO> getPopularAlbums() {
        return popularAlbums;
    }

    public void setPopularAlbums(List<AlbumStreamCountDTO> popularAlbums) {
        this.popularAlbums = popularAlbums;
    }
}
