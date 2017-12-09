package com.cse308.sbuify.song;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface SongRepository  extends CrudRepository<Song, Integer> {

    @Query("Update Song S Set S.playCount = S.playCount + :newStreams WHERE S.id = :songId")
    void incrementPlayCountById(@Param("songId") int songId, @Param("newStreams") int newStreams);

    @Query(value = "SELECT s.*" +
            "FROM song s " +
            "WHERE s.album_id = ?2 " +
            "AND s.id NOT IN (" +
            "   SELECT ps.song_id" +
            "   FROM playlist_songs ps, customer c" +
            "   WHERE ps.playlist_id = c.library_id" +
            "   AND c.id = ?1" +
            ")", nativeQuery = true)
    List<Song> getUnsavedSongsFromAlbum(int customerId, int albumId);

    @Query(value = "SELECT CASE WHEN EXISTS (" +
            "   SELECT *" +
            "   FROM play_queue_songs pqs, customer c" +
            "   WHERE pqs.play_queue_id = c.play_queue_id" +
            "   AND pqs.song_id = ?1" +
            "   AND c.id = ?2" +
            ")" +
            "THEN 1 " +
            "ELSE 0 END", nativeQuery = true)
    Integer isQueuedByUser(Integer songId, Integer userId);

    @Query(value = "SELECT CASE WHEN EXISTS (" +
            "   SELECT *" +
            "   FROM playlist_songs ps, customer c" +
            "   WHERE ps.playlist_id = c.library_id" +
            "   AND ps.song_id = ?1" +
            "   AND c.id = ?2" +
            ")" +
            "THEN 1 " +
            "ELSE 0 END", nativeQuery = true)
    Integer isSavedByUser(Integer songId, Integer userId);

    @Query(value = "SELECT s.* " +
            "FROM song s, album a, artist ar " +
            "WHERE ar.id = :artistId" +
            "   AND a.artist_id = ar.id" +
            "   AND s.album_id = a.id " +
            "ORDER BY s.play_count DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Song> getPopularByArtist(@Param("artistId") Integer artistId);

    @Query(value = "SELECT s.* " +
            "FROM song s" +
            "ORDER BY s.play_count DESC " +
            "LIMIT 50", nativeQuery = true)
    List<Song> getTop50PopularSongs();

    @Query(value = "SELECT s.* " +
            "FROM song s" +
            "WHERE s.active = true" +
            "ORDER BY s.play_count ASC " +
            "LIMIT 50", nativeQuery = true)
    Set<Song> getTop50UnpopularSongs();

    @Query(value = "SELECT s.* " +
            "FROM song s, song_genres sg, genre g" +
            "WHERE s.id = sg.song_id " +
            "   AND sg.genre_id = g.id" +
            "   AND (g.name = :genre " +
            "   OR g.name = :genre2) " +
            "ORDER BY RAND()", nativeQuery = true)
    Set<Song> mixGenre(@Param("genre") String genre, @Param("genre2") String genre2);

    @Query(value = "SELECT s.* " +
            "FROM song s, song_genres sg, genre g" +
            "WHERE s.id = sg.song_id " +
            "   AND sg.genre_id = g.id" +
            "   AND (g.name = :genre " +
            "   OR g.name = :genre2" +
            "   OR g.name = :genre3)" +
            "ORDER BY RAND()", nativeQuery = true)
    Set<Song> mixTripleGenre(@Param("genre") String genre,
                             @Param("genre2") String genre2,
                             @Param("genre3") String genre3);

    @Query(value = "SELECT s.* " +
            "FROM song s, stream st" +
            "WHERE st.customer_id = :customerId " +
            "   AND s.id = st.song_id" +
            "ORDER BY st.time DESC" , nativeQuery = true)
    Set<Song> getRecentlyPlayed(@Param("customerId") Integer customerId);

    @Query(value = "SELECT s.* " +
            "FROM song s, stream st " +
            "WHERE s.id = st.song_id " +
            "ORDER BY st.time DESC", nativeQuery = true)
    Set<Song> getSBUifyRecentStream(@Param("customerId") Integer customerId);

    @Query(value = "SELECT DISTINCT s.*" +
            "FROM song s, album ar,song_featured_artists sfa " +
            "WHERE (sfa.artist_id = :artistId " +
            "   AND s.id = sfa.song_id) " +
            "   OR (ar.artist_id = :artistId " +
            "   AND s.album_id = ar.id) " +
            "ORDER BY  RAND() ", nativeQuery = true)
    List<Song> getArtistOfTheMonth(@Param("artistId") Integer artistId);

    @Query(value = "SELECT DISTINCT s.*" +
            "FROM song s, album ar " +
            "WHERE (ar.artist_id = :artistId " +
            "   OR ar.artist_id = :artistId2)" +
            "   AND s.album_id = ar.id" +
            "ORDER BY  RAND() ", nativeQuery = true)
    List<Song> artistDuo(@Param("artistId") Integer artistId,
                         @Param("artistId2") Integer artistId2);

    @Query(value = "SELECT DISTINCT s.*" +
            "FROM song s, album ar " +
            "WHERE (ar.artist_id = :artistId " +
            "   OR ar.artist_id = :artistId2" +
            "   OR ar.artist_id = :artistId3)" +
            "   AND s.album_id = ar.id " +
            "ORDER BY  RAND() ", nativeQuery = true)
    List<Song> artistTriplet(@Param("artistId") Integer artistId,
                             @Param("artistId2") Integer artistId2,
                             @Param("artistId3") Integer artistId3);
}
