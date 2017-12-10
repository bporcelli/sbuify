package com.cse308.sbuify.artist;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    List<Product> getAllByArtist(Artist artist);
}
