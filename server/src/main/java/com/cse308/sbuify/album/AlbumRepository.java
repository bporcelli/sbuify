package com.cse308.sbuify.album;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

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
}
