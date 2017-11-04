package com.cse308.sbuify.album;

import com.cse308.sbuify.album.Album;
import org.springframework.data.repository.CrudRepository;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

}
