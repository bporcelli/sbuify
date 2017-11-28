package com.cse308.sbuify.playlist;

import static org.junit.Assert.assertNotEquals;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import com.cse308.sbuify.user.UserRepository;

@Controller
@RequestMapping(path = "/api/playlists")
@ConfigurationProperties("playlist")
public class PlaylistController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PlaylistSongRepository playlistSongRepository;

    private static Integer MAX_SONGS = null;

    @Autowired
    public PlaylistController(PlaylistProperties playlistProperties) {
        MAX_SONGS = playlistProperties.getMaxSongs();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        Playlist saved = playlistRepository.save(playlist);
        return new ResponseEntity<Playlist>(saved, HttpStatus.CREATED);
    }

    /**
     * Get information about a Playlist.
     *
     * @param id
     *            The ID of the playlist.
     * @return The playlist if it exists and can be accessed by the authenticated
     *         user, otherwise void.
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
     * Update playlist
     * 
     * @param playlistId
     * @param newPl
     * @return Return HTTP.OK when successful. HTTP.NOT_FOUND when playlist not
     *         found. else HTTP.FORBIDDEN
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    // Overriding sequence diagram
    public ResponseEntity<?> updatePlaylist(@PathVariable("id") Integer strPlaylistId,
            @RequestBody PlaylistInfoRequestWrapper request) {
        Optional<Playlist> dbPlaylist = playlistRepository.findById(strPlaylistId);

        if (!dbPlaylist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        // owner or admin can edit
        if (!isOwnerOrAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        playlist.setName(request.getName());

        playlist.setDescription(request.getDescription());

        playlistRepository.save(playlist);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return HTTP.OK when successful, HTTP.NOT_FOUND when playlist cannot be
     *         found, HTTP.FORBIDDEN otherwise
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

        if (!isOwnerOrAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        playlistRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param playlistId
     * @param toAdd
     * @return HTTP.OK with how many songs added in body for return when successful,
     *         HTTP.NOT_FOUND when playlist cannot be found, HTTP.FORBIDDEN
     *         otherwise
     */
    @PostMapping(path = "/{id}/add")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<Void> addQueueableToPlaylist(
            @PathVariable("id") Integer playlistId,
            @RequestBody Song toAdd
    ) {
        System.out.println("addQueueableToPlaylist");
        
        Optional<Playlist> dbPlaylist = playlistRepository.findById(playlistId);

        if (!dbPlaylist.isPresent()) {
            System.out.println("addQueueableToPlaylist2");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist playlist = dbPlaylist.get();

        System.out.println(playlist);

        Collection<Song> songs = toAdd.getItems();

        List<PlaylistSong> listPls = playlist.getSongs();

        if (listPls.size() + songs.size() > MAX_SONGS) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = authFacade.getCurrentUser();
        boolean isOwnerOrAdmin = playlist.getOwner().equals(user) || user instanceof Admin;
        if (!isOwnerOrAdmin) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Iterator<Song> iterator = songs.iterator();

        while (iterator.hasNext()) {
            Song song = iterator.next();

            PlaylistSong pls = new PlaylistSong(playlist, song);
            // save playlist song to our repo

            playlistSongRepository.save(pls);

            listPls.add(pls);
        }

        playlistRepository.save(playlist);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @param playlistId
     * @param toDelete
     * @return HTTP.OK when successful, HTTP.NOT_FOUND when playlist cannot be
     *         found, HTTP.FORBIDDEN otherwise
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

        if (isOwnerOrAdmin) {
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
