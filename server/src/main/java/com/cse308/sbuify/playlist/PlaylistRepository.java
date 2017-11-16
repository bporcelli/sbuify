package com.cse308.sbuify.playlist;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
    @Query("FROM Playlist s WHERE LOWER(s.name) = LOWER(:keyword)")
    Iterable<Playlist> findByKeyword(@Param("keyword") String keyword);
}
