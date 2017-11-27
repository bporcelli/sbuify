package com.cse308.sbuify.stream;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface StreamRepository extends CrudRepository<Stream, Integer> {
    List<StreamCountDTO> findPlayCounts(LocalDateTime start, LocalDateTime end);
}
