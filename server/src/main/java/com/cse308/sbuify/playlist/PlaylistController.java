package com.cse308.sbuify.playlist;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private AuthFacade authFacade;

    /**
     * Update a playlist.
     */
    @PatchMapping(path = "/{id}")
    public void update(@PathVariable Integer id, @RequestBody Playlist playlist) {
        // todo
    }

    /**
     * Get information about a Playlist.
     *
     * @param id The ID of the playlist.
     * @return The playlist if it exists and can be accessed by the authenticated user, otherwise void.
     */
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> getPlaylist(@PathVariable Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {  // playlist not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!playlist.isHidden() || isOwnerOrAdmin) {  // current user can access playlist
            return new ResponseEntity<>(playlist, HttpStatus.OK);
        }

        // if we reach this point, the user doesn't have access
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
