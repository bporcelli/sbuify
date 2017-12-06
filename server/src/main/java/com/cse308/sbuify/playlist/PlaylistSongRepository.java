package com.cse308.sbuify.playlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

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
}
