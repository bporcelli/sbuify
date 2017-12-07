package com.cse308.sbuify.customer;

import org.springframework.data.repository.CrudRepository;

public interface SavedPlaylistRepository extends CrudRepository<SavedPlaylist, SavedPlaylist.PK> {
    // todo: get saved by user id
}
