package com.cse308.sbuify.customer;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavedPlaylistRepository extends CrudRepository<SavedPlaylist, SavedPlaylist.PK> {
    List<SavedPlaylist> findByCustomer(Customer customer);
}
