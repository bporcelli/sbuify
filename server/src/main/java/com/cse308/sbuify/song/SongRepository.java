package com.cse308.sbuify.song;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SongRepository  extends CrudRepository<Song, Integer> {
	
	@Query("FROM Song s WHERE LOWER(s.name) = LOWER(:keyword)")
	public Iterable<Song> findByKeyword(@Param("keyword") String keyword);
}
