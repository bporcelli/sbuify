package com.cse308.sbuify.playlist;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

@Controller
@RequestMapping(path = "/api/playlists")
@ConfigurationProperties("playlist")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private AuthFacade authFacade;

    private static Integer MAX_SONGS = null;

    @Autowired
    public PlaylistController(PlaylistProperties playlistProperties) {
        MAX_SONGS = playlistProperties.getMaxSongs();
    }

    /**
     * Create a new playlist.
     * @param playlist The playlist to create.
     * @return The playlist as saved in the database.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        // todo: handle image uploads (use image.StorageService)
        Playlist saved = playlistRepository.save(playlist);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Get information about a Playlist.
     * @param id The ID of the playlist.
     * @return The playlist if it exists and can be accessed by the authenticated user, otherwise void.
     */
    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> getPlaylistById(@PathVariable Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) { // playlist not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!isOwnerOrAdmin) { // current user can access playlist
            // if we reach this point, the user doesn't have private access
            if (playlist.isHidden()) {
                // if it is private playlist
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        // if code reaches here, isOwnerOrAdmin or playlist is public
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * Update a playlist.
     * @param id The ID of the playlist to update.
     * @param updated Updated playlist.
     * @return a 200 response if operation is successful, a 404 if the playlist ID is invalid, or a
     *         403 if the current user does not have permission to update the playlist.
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> updatePlaylist(@PathVariable Integer id, @RequestBody Playlist updated) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(Integer.valueOf(id));

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!isOwnerOrAdmin) {  // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        playlist.setName(updated.getName());
        playlist.setDescription(updated.getDescription());
        playlistRepository.save(playlist);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a playlist.
     * @param id The ID of the playlist to delete.
     * @return a 200 response if the operation is successful, a 404 if the playlist ID is invalid, or a
     *         403 if the current user does not have permission to delete the playlist.
     */
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> deletePlaylist(@PathVariable Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!isOwnerOrAdmin) {  // can't delete playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        playlistRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add a song or album to a playlist.
     * @param id Playlist ID.
     * @param toAdd The song or album to add.
     * @return a 200 response with the number of songs added in body on success, a 404 if the playlist ID is
     *         invalid, and a 403 otherwise.
     */
    @PostMapping(path = "/{id}/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> addToPlaylist(@PathVariable Integer id, @RequestBody Queueable toAdd) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>("Invalid playlist ID.", HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!isOwnerOrAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!isOwnerOrAdmin) {  // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Collection<Song> newSongs = toAdd.getItems();
        List<PlaylistSong> existingSongs = playlist.getSongs();

        if (existingSongs.size() + newSongs.size() > MAX_SONGS) {
            return new ResponseEntity<>("Maximum playlist size exceeded.", HttpStatus.BAD_REQUEST);
        }

        for (Song song: newSongs) {
            playlist.add(song);
        }
        playlistRepository.save(playlist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Remove a song or album from a playlist.
     * @param id The ID of the playlist.
     * @param toDelete The song or album to remove.
     * @return a 200 response if the operation succeeds, a 404 if the playlist ID is invalid, a 400 if the
     *         provided song list is invalid, or a 403 otherwise.
     */
    @PostMapping(path = "/{id}/remove")
    public ResponseEntity<?> removeFromPlaylist(@PathVariable Integer id, @RequestBody Queueable toDelete) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

        if (!optionalPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Playlist playlist = optionalPlaylist.get();

        Collection<Song> songs = toDelete.getItems();
        List<PlaylistSong> existingSongs = playlist.getSongs();

        if (existingSongs.size() - songs.size() < 0) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;

        if (!isOwnerOrAdmin) {  // can't edit playlist
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        for (Song song: songs) {
            playlist.remove(song);
        }
        playlistRepository.save(playlist);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
