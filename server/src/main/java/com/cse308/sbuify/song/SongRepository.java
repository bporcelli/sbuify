package com.cse308.sbuify.song;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
}
