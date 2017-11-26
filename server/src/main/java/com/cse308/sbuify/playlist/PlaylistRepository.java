package com.cse308.sbuify.playlist;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<Playlist, Integer> {
    Optional<Playlist> findByOwner_Id(Integer ownerId);
}
