package com.cse308.sbuify.stream;

import com.cse308.sbuify.artist.MonthlyListenersDTO;
import com.cse308.sbuify.artist.QuarterlyRoyaltyDTO;
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

    @Query("SELECT new com.cse308.sbuify.artist.MonthlyListenersDTO(a.artist,  COUNT(s)) " +
            "FROM Stream s , Song song, Album a " +
            "WHERE  s.time >= :time " +
            "   AND song = s.song" +
            "   AND song.album = a "+
            "GROUP BY a.artist")
    List<MonthlyListenersDTO> getNewMonthlyCountAfterTime(@Param("time") LocalDateTime time);

    @Query("SELECT new com.cse308.sbuify.artist.QuarterlyRoyaltyDTO(a.artist,  COUNT(s)) " +
            "FROM Stream s , Song song, Album a " +
            "WHERE  s.time >= :time " +
            "   AND song = s.song" +
            "   AND song.album = a "+
            "GROUP BY a.artist")
    List<QuarterlyRoyaltyDTO> getQuarterlyRoyalty(@Param("time") LocalDateTime time);

}
