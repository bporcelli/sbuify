package com.cse308.sbuify.reports;

import com.cse308.sbuify.album.AlbumStreamCountDTO;
import com.cse308.sbuify.artist.ArtistStreamCountDTO;
import com.cse308.sbuify.customer.SubscriptionRepository;
import com.cse308.sbuify.playlist.PlaylistStreamCountDTO;
import com.cse308.sbuify.stream.StreamCountDTO;
import com.cse308.sbuify.stream.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report Generator.
 *
 * Responsible for generating reports.
 */
@Controller
@RequestMapping(path = "/api/reports")
public class ReportController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private StreamRepository streamRepository;

    /**
     * Get the data for the subscribers report.
     * @param window Reporting window (days).
     * @return a 200 response containing the report data.
     */
    @GetMapping(path = "/subscribers")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody SubscribersReport getSubscribersReport(@RequestParam Integer window) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(window);

        Integer subsGained = subscriptionRepository.countAllByStartBetween(start, end);
        Integer subsLost = subscriptionRepository.countAllByEndBetween(start, end);
        Integer totalSubs = subscriptionRepository.countAllByEndIsNull();

        return new SubscribersReport(subsGained, subsLost, totalSubs);
    }

    /**
     * Get the data for the trends report.
     * @param window Reporting window (days).
     * @return a 200 response containing the report data.
     */
    @GetMapping(path = "/trends")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody TrendsReport getTrendsReport(@RequestParam Integer window) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(window);

        PageRequest topFiveRequest = PageRequest.of(0, 5);

        List<StreamCountDTO> topSongs = streamRepository.getTopSongsForPeriod(start, end, topFiveRequest);
        List<ArtistStreamCountDTO> topArtists = streamRepository.getTopArtistsForPeriod(start, end, topFiveRequest);
        List<PlaylistStreamCountDTO> topPlaylists = streamRepository.getTopPlaylistsForPeriod(start, end, topFiveRequest);

        return new TrendsReport(topSongs, topArtists, topPlaylists);
    }

    /**
     * Get the data for the artist report.
     * @param id Artist ID.
     * @param window Reporting window (days).
     * @return a 200 response containing the report data.
     */
    @GetMapping(path = "/artist/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ArtistReport getTrendsReport(@PathVariable Integer id, @RequestParam Integer window) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(window);

        PageRequest topTenReq = PageRequest.of(0, 10);

        Long freeStreams = streamRepository.countAllBySong_Album_Artist_IdAndPremiumIsFalseAndTimeBetween(id, start, end);
        Long premiumStreams = streamRepository.countAllBySong_Album_Artist_IdAndPremiumIsTrueAndTimeBetween(id, start, end);
        List<StreamCountDTO> popularSongs = streamRepository.getTopSongsForPeriodAndArtist(start, end, id, topTenReq);
        List<AlbumStreamCountDTO> popularAlbums = streamRepository.getTopAlbumsForPeriodAndArtist(start, end, id, topTenReq);

        return new ArtistReport(freeStreams, premiumStreams, popularSongs, popularAlbums);
    }

    /**
     * Get the data for the song report.
     * @param id ID of song for report.
     * @param window Reporting window (days).
     * @return a 200 response containing the report data.
     */
    @GetMapping(path = "/song/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody SongReport getSongReport(@PathVariable Integer id, @RequestParam Integer window) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(window);
        Long freeStreams = streamRepository.countAllBySong_IdAndPremiumIsFalseAndTimeBetween(id, start, end);
        Long premiumStreams = streamRepository.countAllBySong_IdAndPremiumIsTrueAndTimeBetween(id, start, end);
        return new SongReport(freeStreams, premiumStreams);
    }
}