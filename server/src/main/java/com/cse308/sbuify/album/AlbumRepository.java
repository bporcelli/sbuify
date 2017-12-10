package com.cse308.sbuify.album;

import com.cse308.sbuify.artist.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlbumRepository extends PagingAndSortingRepository<Album, Integer> {

    @Query(value = "SELECT a.*" +
            "   FROM album a, album_songs sa, song_genres sg, song s" +
            "   WHERE a.id = sa.album_id" +
            "       AND sa.song_id = s.id" +
            "       AND s.id = sg.song_id" +
            "       AND sg.genre_id = ?1" +
            "   GROUP BY a.id" +
            "   ORDER BY SUM(s.play_count) DESC" +
            "   LIMIT ?2", nativeQuery = true)
    List<Album> findPopularAlbumsByGenreId(Integer id, int numAlbums);

    @Query(value = "SELECT a.*" +
            "   FROM album a, album_songs sa, song_genres sg" +
            "   WHERE a.id = sa.album_id" +
            "       AND sa.song_id = sg.song_id" +
            "       AND sg.genre_id = ?1" +
            "   GROUP BY a.id" +
            "   ORDER BY a.release_date DESC" +
            "   LIMIT ?2", nativeQuery = true)
    List<Album> findRecentAlbumsByGenreId(Integer id, int numAlbums);

    @Query(
            value = "SELECT * FROM album a ORDER BY release_date DESC \n-- #pageable\n",
            countQuery = "SELECT COUNT(*) FROM album a ORDER BY release_date DESC",
            nativeQuery = true
    )
    Page<Album> findRecent(Pageable page);

    @Query(
            value = "SELECT * FROM album WHERE id IN (" +
            "   SELECT DISTINCT s.album_id" +
            "   FROM song s, customer c, playlist_songs ps" +
            "   WHERE ps.playlist_id = c.library_id" +
            "       AND ps.song_id = s.id" +
            "       AND c.id = :customerId" +
            ")\n-- #pageable\n",
            countQuery = "SELECT COUNT(*) FROM album WHERE id IN (" +
            "   SELECT DISTINCT s.album_id" +
            "   FROM song s, customer c, playlist_songs ps" +
            "   WHERE ps.playlist_id = c.library_id" +
            "       AND ps.song_id = s.id" +
            "       AND c.id = :customerId" +
            ")",
            nativeQuery = true
    )
    Page<Album> getSavedByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);

    @Query(value = "SELECT COUNT(ps.song_id) = a.num_songs " +
            "FROM album a, album_songs aso, playlist_songs ps, customer c " +
            "WHERE ps.playlist_id = c.library_id" +
            "   AND aso.song_id = ps.song_id" +
            "   AND aso.album_id = a.id" +
            "   AND a.id = ?2" +
            "   AND c.id = ?1", nativeQuery = true)
    Integer isSavedByUser(Integer userId, Integer albumId);


    @Query(
            value = "SELECT a.* FROM album a, song s, stream st " +
                    "WHERE s.id = st.song_id " +
                    "AND s.album_id = a.id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY a.id " +
                    "ORDER BY MAX(st.time) DESC\n -- #pageable\n",
            countQuery = "SELECT COUNT(a.id) FROM album a, song s, stream st " +
                    "WHERE s.id = st.song_id " +
                    "AND s.album_id = a.id " +
                    "AND st.customer_id = :customerId " +
                    "GROUP BY a.id",
            nativeQuery = true
    )
    Page<Album> getRecentlyPlayedByCustomer(@Param("customerId") Integer customerId, Pageable pageable);

    List<Album> getAlbumsByArtist(Artist artist);
}
