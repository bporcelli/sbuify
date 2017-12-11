package com.cse308.sbuify.stream;

import com.cse308.sbuify.artist.MonthlyListenersDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StreamRepository extends CrudRepository<Stream, Integer> {
    List<Stream> getAllByTimeAfter(LocalDateTime streamsAfter);

    @Query("SELECT new com.cse308.sbuify.stream.StreamCountDTO(s.song,  COUNT(s)) " +
           "FROM Stream s WHERE s.time >= :time " +
           "GROUP BY s.song")
    List<StreamCountDTO> getNewStreamsAfterTime(@Param("time") LocalDateTime time);
}
