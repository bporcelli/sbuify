package com.cse308.sbuify.album;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

    @Query("FROM Album s WHERE LOWER(s.name) = LOWER(:keyword)")
    Iterable<Album> findByKeyword(@Param("keyword") String keyword);
}
