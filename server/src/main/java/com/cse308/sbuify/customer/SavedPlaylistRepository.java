package com.cse308.sbuify.customer;

import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistFolder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SavedPlaylistRepository extends CrudRepository<SavedPlaylist, SavedPlaylist.PK> {
    List<SavedPlaylist> findByCustomerAndParent(Customer customer, PlaylistFolder folder);
    List<SavedPlaylist> findByParent(PlaylistFolder folder);
    void deleteAllByParent(PlaylistFolder folder);
    void deleteAllByPlaylist(Playlist playlist);
}
