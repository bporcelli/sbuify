package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistSongRepository extends CrudRepository<PlaylistSong, Integer> {

    @Query(
        value = "SELECT ps.* " +
                "FROM playlist_songs ps, customer c " +
                "WHERE ps.playlist_id = c.library_id " +
                "AND c.id = :customerId \n-- #pageable\n",
        countQuery = "SELECT COUNT(ps.song_id) " +
                "FROM playlist_songs ps, customer c " +
                "WHERE ps.playlist_id = c.library_id " +
                "AND c.id = :customerId",
        nativeQuery = true
    )
    Page<PlaylistSong> getLibrarySongs(@Param("customerId") Integer customerId, Pageable pageable);

    @Modifying
    void deleteAllByPlaylistAndSong_Album(Playlist playlist, Album album);

    List<PlaylistSong> getAllByPlaylistAndSong_Album(Playlist playlist, Album album);

    @Modifying
    void deleteAllByPlaylistAndSong_Album_Artist_Id(Playlist playlist, Integer artistId);

    Integer countAllByPlaylist(Playlist playlist);
}
