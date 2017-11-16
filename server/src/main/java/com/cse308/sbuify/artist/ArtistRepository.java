package com.cse308.sbuify.artist;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends CrudRepository<Artist, Integer> {
    @Query("FROM Artist a WHERE a.owner = NULL and LOWER(a.name) = LOWER(:keyword)")
    Iterable<Artist> findByKeyword(@Param("keyword") String keyword);

    @Query("FROM Artist a WHERE a.owner != NULL and LOWER(a.name) = LOWER(:keyword)")
    Iterable<Artist> findByKeywordUnmanaged(@Param("keyword") String keyword);
}
