package com.cse308.sbuify.artist;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArtistRepository extends CrudRepository<Artist, Integer> {

    @Query(value = "SELECT a.*" +
            "FROM artist a " +
            "WHERE id IN (" +
            "   SELECT DISTINCT al.artist_id" +
            "   FROM album al, playlist_songs ps, song s, customer c" +
            "   WHERE ps.playlist_id = c.library_id" +
            "       AND ps.song_id = s.id" +
            "       AND al.id = s.album_id" +
            "       AND c.id = ?1" +
            ")", nativeQuery = true)
    List<Artist> getSavedByCustomerId(Integer customerId);
}
