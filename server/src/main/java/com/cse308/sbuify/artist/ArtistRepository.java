package com.cse308.sbuify.artist;

import java.util.Optional;

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

    @Query(
            value = "SELECT ar.* " +
                    "FROM album a, song s, stream st, artist ar " +
                    "WHERE s.id = st.song_id " +
                    "AND s.album_id = a.id " +
                    "AND a.artist_id = ar.id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY ar.id " +
                    "ORDER BY MAX(st.time) DESC\n -- #pageable\n",
            countQuery = "SELECT COUNT(ar.id) " +
                    "FROM album a, song s, stream st, artist ar " +
                    "WHERE s.id = st.song_id " +
                    "AND s.album_id = a.id " +
                    "AND a.artist_id = ar.id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY ar.id ",
            nativeQuery = true
    )
    Page<Artist> getRecentlyPlayedByCustomer(@Param("customerId") Integer customerId, Pageable pageable);

    @Query(
            value = "SELECT a.* " +
                    "FROM artist a LEFT JOIN (" +
                    "   SELECT a2.id AS aid, COUNT(fa.song_id) AS cnt" +
                    "   FROM artist a2, album al, song s, song_featured_artists fa" +
                    "   WHERE fa.song_id = s.id" +
                    "       AND s.album_id = al.id" +
                    "       AND al.artist_id = :artistId" +
                    "       AND fa.artist_id = a2.id" +
                    "    GROUP BY a2.id " +
                    ") AS b " +
                    "ON a.id = b.aid " +
                    "WHERE a.id != :artistId " +
                    "ORDER BY b.cnt DESC\n -- #pageable\n",
            countQuery = "SELECT COUNT(*) FROM artist WHERE id != :artistId",
            nativeQuery = true
    )
    Page<Artist> getRelatedByArtistId(@Param("artistId") Integer artistId, Pageable pageable);

    @Query(value = "SELECT * FROM artist ORDER BY ID ASC LIMIT :index, 1", nativeQuery = true)
    Artist getArtistByOffset(@Param("index") Long index);
    
    Optional<Artist> findByName(String artistName);
}
