package com.cse308.sbuify.song;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    @Query(value = "SELECT * FROM genre ORDER BY id DESC LIMIT :offset, 1", nativeQuery = true)
    Genre getByOffset(@Param("offset") Long offset);
}
