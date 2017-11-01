package com.cse308.sbuify.repository;

import org.springframework.data.repository.CrudRepository;

import com.cse308.sbuify.domain.Album;

public interface AlbumRepository extends CrudRepository<Album, Integer> {

}
