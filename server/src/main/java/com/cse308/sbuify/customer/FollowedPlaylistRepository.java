package com.cse308.sbuify.customer;

import com.cse308.sbuify.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FollowedPlaylistRepository extends CrudRepository<FollowedPlaylist, Integer> {
    Optional<FollowedPlaylist> findByCustomerAndPlaylist_Id(User customer, Integer playlistId);

    boolean existsByCustomerAndPlaylist_Id(User customer, Integer playlistId);
}
