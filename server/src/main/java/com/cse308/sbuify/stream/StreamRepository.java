package com.cse308.sbuify.stream;

import com.cse308.sbuify.album.AlbumStreamCountDTO;
import com.cse308.sbuify.artist.ArtistStreamCountDTO;
import com.cse308.sbuify.artist.MonthlyListenersDTO;
import com.cse308.sbuify.label.payment.QuarterlyRoyaltyDTO;
import com.cse308.sbuify.playlist.PlaylistStreamCountDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StreamRepository extends CrudRepository<Stream, Integer> {
    List<Stream> getAllByTimeAfter(LocalDateTime streamsAfter);

    @Query("SELECT new com.cse308.sbuify.stream.StreamCountDTO(s.song,  COUNT(s)) " +
           "FROM Stream s WHERE s.time >= :time " +
           "GROUP BY s.song")
    List<StreamCountDTO> getNewStreamsAfterTime(@Param("time") LocalDateTime time);

    @Query("SELECT new com.cse308.sbuify.artist.MonthlyListenersDTO(a.artist,  COUNT(s)) " +
            "FROM Stream s, Song song, Album a " +
            "WHERE s.time >= :time " +
            "   AND song = s.song" +
            "   AND song.album = a "+
            "GROUP BY a.artist")
    List<MonthlyListenersDTO> getNewMonthlyCountAfterTime(@Param("time") LocalDateTime time);

    @Query("SELECT new com.cse308.sbuify.label.payment.QuarterlyRoyaltyDTO(" +
            "   label, " +
            "   SUM(CASE WHEN s.premium = true THEN 1 ELSE 0 END), " +
            "   SUM(CASE WHEN s.premium = false THEN 1 ELSE 0 END)" +
            ") " +
            "FROM Stream s, Song song, Label label, LabelSong ls " +
            "WHERE s.time >= :start AND s.time < :end " +
            "   AND song = s.song" +
            "   AND song = ls.song" +
            "   AND label = ls.label "+
            "GROUP BY ls.label")
    List<QuarterlyRoyaltyDTO> getQuarterlyRoyalty(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT new com.cse308.sbuify.artist.ArtistStreamCountDTO(a.artist,  COUNT(s)) " +
            "FROM Stream s, Song song, Album a " +
            "WHERE s.time >= :start AND s.time < :end " +
            "   AND song = s.song" +
            "   AND song.album = a "+
            "GROUP BY a.artist " +
            "ORDER BY COUNT(s) DESC")
    List<ArtistStreamCountDTO> getTopArtistsForPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("SELECT new com.cse308.sbuify.stream.StreamCountDTO(s.song,  COUNT(s)) " +
            "FROM Stream s " +
            "WHERE s.time >= :start AND s.time < :end " +
            "GROUP BY s.song " +
            "ORDER BY COUNT(s) DESC")
    List<StreamCountDTO> getTopSongsForPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    @Query("SELECT new com.cse308.sbuify.playlist.PlaylistStreamCountDTO(s.playlist,  COUNT(s)) " +
            "FROM Stream s " +
            "WHERE s.time >= :start AND s.time < :end " +
            "AND s.playlist <> NULL " +
            "GROUP BY s.playlist " +
            "ORDER BY COUNT(s) DESC")
    List<PlaylistStreamCountDTO> getTopPlaylistsForPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, Pageable pageable);

    Long countAllBySong_IdAndPremiumIsFalseAndTimeBetween(Integer songId, LocalDateTime start, LocalDateTime end);

    Long countAllBySong_IdAndPremiumIsTrueAndTimeBetween(Integer songId, LocalDateTime start, LocalDateTime end);

    Long countAllBySong_Album_Artist_IdAndPremiumIsFalseAndTimeBetween(Integer aristId, LocalDateTime start, LocalDateTime end);

    Long countAllBySong_Album_Artist_IdAndPremiumIsTrueAndTimeBetween(Integer artistId, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new com.cse308.sbuify.stream.StreamCountDTO(s.song,  COUNT(s)) " +
            "FROM Stream s, Song song, Album a " +
            "WHERE s.time >= :start AND s.time < :end " +
            "   AND s.song = song" +
            "   AND song.album = a" +
            "   AND a.artist.id = :id " +
            "GROUP BY s.song " +
            "ORDER BY COUNT(s) DESC")
    List<StreamCountDTO> getTopSongsForPeriodAndArtist(@Param("start") LocalDateTime start,
                                                       @Param("end") LocalDateTime end,
                                                       @Param("id") Integer artistId,
                                                       Pageable pageable);

    @Query("SELECT new com.cse308.sbuify.album.AlbumStreamCountDTO(a,  COUNT(s)) " +
            "FROM Stream s, Song song, Album a " +
            "WHERE s.time >= :start AND s.time < :end " +
            "   AND s.song = song" +
            "   AND song.album = a" +
            "   AND a.artist.id = :id " +
            "GROUP BY a.id " +
            "ORDER BY COUNT(s) DESC")
    List<AlbumStreamCountDTO> getTopAlbumsForPeriodAndArtist(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end,
                                                             @Param("id") Integer artistId,
                                                             Pageable pageable);
}
