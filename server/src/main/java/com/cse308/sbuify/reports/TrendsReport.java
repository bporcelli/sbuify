package com.cse308.sbuify.reports;

import com.cse308.sbuify.artist.ArtistStreamCountDTO;
import com.cse308.sbuify.playlist.PlaylistStreamCountDTO;
import com.cse308.sbuify.stream.StreamCountDTO;

import java.util.List;

public class TrendsReport {

    /** Top 5 songs over the reporting period with the number of streams */
    private List<StreamCountDTO> popularSongs;

    /** Top 5 artists over the period with the number of streams */
    private List<ArtistStreamCountDTO> popularArtists;

    /** Top 5 playlists over the period with the number of streams */
    private List<PlaylistStreamCountDTO> popularPlaylists;

    public TrendsReport(
        List<StreamCountDTO> popularSongs,
        List<ArtistStreamCountDTO> popularArtists,
        List<PlaylistStreamCountDTO> popularPlaylists
    ) {
        this.popularSongs = popularSongs;
        this.popularArtists = popularArtists;
        this.popularPlaylists = popularPlaylists;
    }

    public List<StreamCountDTO> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<StreamCountDTO> popularSongs) {
        this.popularSongs = popularSongs;
    }

    public List<ArtistStreamCountDTO> getPopularArtists() {
        return popularArtists;
    }

    public void setPopularArtists(List<ArtistStreamCountDTO> popularArtists) {
        this.popularArtists = popularArtists;
    }

    public List<PlaylistStreamCountDTO> getPopularPlaylists() {
        return popularPlaylists;
    }

    public void setPopularPlaylists(List<PlaylistStreamCountDTO> popularPlaylists) {
        this.popularPlaylists = popularPlaylists;
    }
}
