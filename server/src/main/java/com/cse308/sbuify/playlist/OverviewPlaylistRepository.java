package com.cse308.sbuify.playlist;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

public interface OverviewPlaylistRepository extends CrudRepository<OverviewPlaylist, Integer> {
    @Modifying
    void deleteByType(OverviewPlaylistType type);
}
