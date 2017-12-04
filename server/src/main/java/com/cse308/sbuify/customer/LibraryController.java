package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.playlist.PlaylistSong;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
     * Get the songs in the customer's library.
     * @return a 200 response with a list of songs in the body.
     */
    @GetMapping
    public @ResponseBody TypedCollection getSongs() {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Playlist library = customer.getLibrary();
        return new TypedCollection(library.getSongs(), PlaylistSong.class);
    }

    /**
     * Add a song to the customer's library.
     * @param id ID of the song to add.
     * @return a 201 response with the saved song in the body, 400 if song is already in library,otherwise a 404 if the song is not found.
     */
    @PostMapping(path = "/{id}")
    public ResponseEntity<?> saveToLibrary(@PathVariable Integer id) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        Optional<Song> optionalSong = songRepo.findById(id);

        if (!optionalSong.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Song song = optionalSong.get();

        Playlist lib = customer.getLibrary();
        // song already in customer library
        if(containsId(lib.getSongs(), id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        PlaylistSong saved = lib.add(song);

        playlistRepo.save(lib);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Delete a song from the customer's library.
     * @param id Song ID.
     * @return an empty 200 response on success, otherwise a 404.
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeFromLibrary(@PathVariable Integer id) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Playlist library = customer.getLibrary();

        Optional<Song> optionalSong = songRepo.findById(id);

        if (!optionalSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlaylistSong deleted = library.remove(optionalSong.get());

        if (deleted == null) {  // song wasn't in library
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        playlistRepo.save(library);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected static boolean containsId(List<PlaylistSong> list, Integer id){
        return list.stream().anyMatch(object -> object.getSong().getId().equals(id));
    }
}
