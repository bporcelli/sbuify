package com.cse308.sbuify.playlist;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/playlists")
public class PlaylistController {
    /**
     * Update a playlist.
     */
    @PatchMapping(path = "/{id}")
    public void update(@PathVariable Integer id, @RequestBody Playlist playlist) {
        // todo
    }
}
