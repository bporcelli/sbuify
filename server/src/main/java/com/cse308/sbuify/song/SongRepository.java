package com.cse308.sbuify.song;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SongRepository  extends CrudRepository<Song, Integer> {

    @Query("Update Song S Set S.play_count = S.play_count + :newStreams WHERE S.id = :songId ;")
    void incrementPlayCountById(@Param("songId") int songId, int newStreams);
}
