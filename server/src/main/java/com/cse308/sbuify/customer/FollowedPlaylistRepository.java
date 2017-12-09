package com.cse308.sbuify.customer;

import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistFolder;
import com.cse308.sbuify.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FollowedPlaylistRepository extends CrudRepository<FollowedPlaylist, Integer> {
    boolean existsByCustomerAndPlaylist_Id(User customer, Integer playlistId);

    Optional<FollowedPlaylist> findByCustomerAndPlaylist_Id(User customer, Integer playlistId);

    List<FollowedPlaylist> findByCustomerAndParent(Customer customer, PlaylistFolder folder);

    List<FollowedPlaylist> findByParent(PlaylistFolder folder);

    void deleteAllByParent(PlaylistFolder folder);

    void deleteAllByPlaylist(Playlist playlist);
}
