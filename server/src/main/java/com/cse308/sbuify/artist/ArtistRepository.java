package com.cse308.sbuify.artist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends CrudRepository<Artist, Integer> {

    @Query(
        value = "SELECT a.*" +
        "FROM artist a " +
        "WHERE id IN (" +
        "   SELECT DISTINCT al.artist_id" +
        "   FROM album al, playlist_songs ps, song s, customer c" +
        "   WHERE ps.playlist_id = c.library_id" +
        "       AND ps.song_id = s.id" +
        "       AND al.id = s.album_id" +
        "       AND c.id = :customerId" +
        ")\n -- #pageable\n",
        countQuery = "SELECT COUNT(a.id)" +
        "FROM artist a " +
        "WHERE id IN (" +
        "   SELECT DISTINCT al.artist_id" +
        "   FROM album al, playlist_songs ps, song s, customer c" +
        "   WHERE ps.playlist_id = c.library_id" +
        "       AND ps.song_id = s.id" +
        "       AND al.id = s.album_id" +
        "       AND c.id = :customerId" +
        ")",
        nativeQuery = true
    )
    Page<Artist> getSavedByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);
}
