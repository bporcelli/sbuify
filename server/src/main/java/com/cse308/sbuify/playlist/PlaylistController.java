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

import javax.xml.ws.Response;
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
     *
     * @param playlistId
     * @return HTTP.OK, user is owner or admin, playlist is public. HTTP.Not_Found, Id does not exist or playlist is hidden
     */
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> getPlaylist(@PathVariable("id") String playlistId) {

        User user = authFacade.getCurrentUser();

        Optional<Playlist> dbPlaylist = playlistRepository.findById(Integer.valueOf(playlistId));

        // playlist not found
        if (!dbPlaylist.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();
        // if user is owner they have access
        if(playlist.getOwner().equals(user) || user instanceof Admin){
            return new ResponseEntity<>(playlist, HttpStatus.OK);
        }
        // if playlist is hidden, playlist cannot be viewed
        if (playlist.isHidden()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(playlist, HttpStatus.OK);

    }


}
