package com.cse308.sbuify.customer;

import java.util.Collection;
import java.util.Optional;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.playlist.PlaylistSong;
import org.apache.http.protocol.HTTP;
import org.jboss.jdeparser.FormatPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;

@Controller
@RequestMapping(path = "/api/customer/songs")
public class LibraryController {
    @Autowired
    private PlaylistRepository playlistRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AuthFacade authFacade;

    /**
     * Get customer library songs
     * @return HTTP.OK when successful
     */
    @GetMapping
    public @ResponseBody ResponseEntity<?> getSongs() {
        Customer customer = (Customer) authFacade.getCurrentUser();

        Playlist library = customer.getLibrary();

        TypedCollection playlistSongs = new TypedCollection(library.getSongs(), PlaylistSong.class);

        return new ResponseEntity<>(playlistSongs, HttpStatus.OK);
    }

    /**
     * Add a song to library
     * @param songId
     * @return HTTP.OK when successful, HTTP.NOT_FOUND when song not found
     */
    @PostMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<?> saveToLibrary(@PathVariable(value = "id") Integer songId) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        Optional<Song> dbSong = songRepo.findById(songId);

        if (!dbSong.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = dbSong.get();

        Playlist lib = customer.getLibrary();

        lib.add(song);

        playlistRepo.save(lib);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a song from library
     * @param songToRmId
     * @return HTTP.CREATED when successful, HTTP.NOT_FOUND when song is not found
     */
    @DeleteMapping(path = "/{id}")
    public @ResponseBody ResponseEntity<?> removeFromLibrary(@PathVariable(value = "id") Integer songToRmId) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        Playlist library = customer.getLibrary();

        Optional<Song> songToRm = songRepo.findById(songToRmId);

        if (!songToRm.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = songToRm.get();

        PlaylistSong deleted = library.remove(song);

        if (deleted == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        playlistRepo.save(library);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
