package com.cse308.sbuify.stream;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface StreamRepository extends CrudRepository<Stream, Integer> {
    List<Stream> getAllByTimeAfter(LocalDateTime streamsAfter);
}
