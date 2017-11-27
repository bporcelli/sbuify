package com.cse308.sbuify.playlist;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

@Controller
@RequestMapping(path = "/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private AuthFacade authFacade;
    
    @PostMapping
    public ResponseEntity<?> createPlaylist(@RequestBody Playlist playlist) {
        Playlist saved = playlistRepository.save(playlist);
        return new ResponseEntity<Playlist>(saved, HttpStatus.CREATED);
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
    
    /**
     * Update a playlist.
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    // Overriding sequence diagram
    public ResponseEntity<?> update(@PathVariable Integer playlistId, @RequestBody Playlist newPl) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        Optional<Playlist> dbPlaylist = playlistRepository.findById(Integer.valueOf(playlistId));

        if (!dbPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        if (!playlist.getOwner().equals(cust)) {
            return new ResponseEntity<>(playlist, HttpStatus.FORBIDDEN);
        }

        playlist.setName(newPl.getName());
        playlist.setDescription(newPl.getDescription());

        return new ResponseEntity<>(HttpStatus.OK);
    }
    

    /**
     * Delete a playlist.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @RequestBody Playlist playlist) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        Optional<Playlist> optPl = playlistRepository.findById(id);
        if (!optPl.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist p = optPl.get();

        User owner = p.getOwner();
        
        if(!cust.equals(owner)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        
        playlistRepository.deleteById(id);;

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * Add a song or album to a playlist
     */
    @PostMapping(path = "/{id}/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> addToPlaylist(@PathVariable("id") String playlistId, @RequestBody Queueable toAdd) {

        Optional<Playlist> dbPlaylist = playlistRepository.findById(Integer.valueOf(playlistId));

        if (!dbPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        Collection<Song> songs = toAdd.getItems();
        Iterator<Song> iterator = songs.iterator();

        // Seq. diagram: playlist.getSavedSongList();
        // Equivalent: playlist.getSongs();
        List<PlaylistSong> listPls = playlist.getSongs();

        int MAX_NUM_SONGS = getMaxSongs();

        if (listPls.size() + songs.size() > MAX_NUM_SONGS) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        while (iterator.hasNext()) {
            Song song = iterator.next();

            PlaylistSong pls = new PlaylistSong(playlist, song);

            listPls.add(pls);
        }

        playlistRepository.save(playlist);

        return new ResponseEntity<>(songs.size(), HttpStatus.OK);
    }

    /**
     * Remove a song or album from a playlist
     */
    @PostMapping(path = "/{id}/remove")
    public ResponseEntity<?> rmQueueableFromPlaylist(@PathVariable Integer id, @RequestBody Queueable toAdd) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Helper method to get the max songs in a playlist
     */

    private int getMaxSongs() {
        // TODO
        return 100;
    }
}
