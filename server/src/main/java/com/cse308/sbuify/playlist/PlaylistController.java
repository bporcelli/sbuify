package com.cse308.sbuify.playlist;

import java.util.Collection;
import java.util.Iterator;
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
import com.cse308.sbuify.customer.Customer;
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

    @Autowired
    private  PlaylistSongRepository playlistSongRepository;

    private static Integer MAX_SONGS = null;

    @Autowired
    public PlaylistController(PlaylistProperties playlistProperties){
        MAX_SONGS = playlistProperties.getMaxSongs();
    }

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
     *  Update playlist
     * @param playlistId
     * @param newPl
     * @return Return HTTP.OK when successful. HTTP.NOT_FOUND when playlist not found. else HTTP.FORBIDDEN
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    // Overriding sequence diagram
    public ResponseEntity<?> updatePlaylist(@PathVariable Integer playlistId, @RequestBody Playlist newPl) {
        Optional<Playlist> dbPlaylist = playlistRepository.findById(Integer.valueOf(playlistId));

        if (!dbPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        User user =  authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        // owner or admin can edit
        if (isOwnerOrAdmin) {

            playlist.setName(newPl.getName());

            playlist.setDescription(newPl.getDescription());

            playlistRepository.save(playlist);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }


    /**
     *
     * @param id
     * @return HTTP.OK when successful, HTTP.NOT_FOUND when playlist cannot be found, HTTP.FORBIDDEN otherwise
     */
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> deletePlaylist(@PathVariable Integer id) {
        Optional<Playlist> optPl = playlistRepository.findById(id);

        if (!optPl.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = authFacade.getCurrentUser();

        Playlist playlist = optPl.get();


        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        
        if(isOwnerOrAdmin) {

            playlistRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    /**
     *
     * @param playlistId
     * @param toAdd
     * @return HTTP.OK with how many songs added in body for return when successful, HTTP.NOT_FOUND when playlist cannot be found, HTTP.FORBIDDEN otherwise
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

        List<PlaylistSong> listPls = playlist.getSongs();

        if (listPls.size() + songs.size() > MAX_SONGS) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }


        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;

        if(isOwnerOrAdmin) {

            Iterator<Song> iterator = songs.iterator();

            while (iterator.hasNext()) {
                Song song = iterator.next();

                PlaylistSong pls = new PlaylistSong(playlist, song);
                // save playlist song to our repo

                playlistSongRepository.save(pls);

                listPls.add(pls);
            }

            playlistRepository.save(playlist);
            // return the amt of songs added
            return new ResponseEntity<>(songs.size(),HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    /**
     *
     * @param playlistId
     * @param toDelete
     * @return HTTP.OK when successful, HTTP.NOT_FOUND when playlist cannot be found, HTTP.FORBIDDEN otherwise
     */
    @PostMapping(path = "/{id}/remove")
    public ResponseEntity<?> rmQueueableFromPlaylist(@PathVariable String playlistId, @RequestBody Queueable toDelete) {
        Optional<Playlist> dbPlaylist = playlistRepository.findById(Integer.valueOf(playlistId));

        if (!dbPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        Collection<Song> songs = toDelete.getItems();

        List<PlaylistSong> listPls = playlist.getSongs();

        if (listPls.size() - songs.size() < 0) {

            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }


        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;

        if(isOwnerOrAdmin) {
            Iterator<Song> iterator = songs.iterator();
            while (iterator.hasNext()) {
                Song song = iterator.next();
                PlaylistSong delete = playlist.remove(song);

                if (delete == null) {
                    return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
                }

                playlistSongRepository.delete(delete);
            }

            playlistRepository.save(playlist);

            return new ResponseEntity<>(HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

}
