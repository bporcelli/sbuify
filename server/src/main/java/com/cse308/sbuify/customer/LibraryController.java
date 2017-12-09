package com.cse308.sbuify.customer;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.playlist.*;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/customer/library")
public class LibraryController {

    private final Integer ITEMS_PER_PAGE;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private PlaylistSongRepository playlistSongRepo;

    @Autowired
    public LibraryController(PlaylistProperties properties) {
        ITEMS_PER_PAGE = properties.getSongsPerPage();
    }

    /**
     * Get the songs in the customer's library.
     * @param page Page index.
     * @return a 200 response with a list of songs in the body.
     */
    @GetMapping("/songs")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getSongs(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = getCurrentCustomer();

        Page<PlaylistSong> result = playlistSongRepo.getLibrarySongs(customer.getId(),
                PageRequest.of(page, ITEMS_PER_PAGE));
        List<PlaylistSong> songs = new ArrayList<>();

        for (PlaylistSong ps: result) {
            songs.add(ps);
        }
        return new TypedCollection(songs, PlaylistSong.class);
    }

    /**
     * Add a song to the customer's library.
     * @param id ID of the song to add.
     * @return a 201 response with the saved song in the body, a 400 if song is already in library,
     *         or a 404 if the song is not found.
     */
    @PostMapping(path = "/songs/{id}")
    public ResponseEntity<?> saveSong(@PathVariable Integer id) {
        Customer customer = getCurrentCustomer();

        Optional<Song> optionalSong = songRepo.findById(id);

        if (!optionalSong.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = optionalSong.get();
        Playlist library = customer.getLibrary();

        if (playlistSongRepo.existsByPlaylistAndSong(library, song)) {  // song is already saved
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        PlaylistSong saved = new PlaylistSong(library, song);
        saved = playlistSongRepo.save(saved);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Delete a song from the customer's library.
     * @param id Song ID.
     * @return an empty 200 response on success, otherwise a 404.
     */
    @DeleteMapping(path = "/songs/{id}")
    @Transactional
    public ResponseEntity<?> removeSong(@PathVariable Integer id) {
        Customer customer = getCurrentCustomer();
        Playlist library = customer.getLibrary();

        Optional<Song> optionalSong = songRepo.findById(id);

        if (!optionalSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = optionalSong.get();

        if (!playlistSongRepo.existsByPlaylistAndSong(library, song)) {  // song wasn't in library
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        playlistSongRepo.deleteByPlaylistAndSong(library, song);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Return all albums in the user's library.
     * @param page Page index.
     * @return a 200 response containing a list of artists on success.
     */
    @GetMapping(path = "/albums")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getAlbums(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = getCurrentCustomer();

        Page<Album> results = albumRepo.getSavedByCustomerId(customer.getId(), PageRequest.of(page, ITEMS_PER_PAGE));
        List<Album> albums = new ArrayList<>();

        for (Album album: results) {
            albums.add(album);
        }
        return new TypedCollection(albums, Album.class);
    }

    /**
     * Add an album to the user's library.
     * @return an empty 200 response on success.
     */
    @PostMapping(path = "/albums/{albumId}")
    public ResponseEntity<?> addAlbum(@PathVariable Integer albumId) {
        Customer customer = getCurrentCustomer();

        Optional<Album> optionalAlbum = albumRepo.findById(albumId);

        if (!optionalAlbum.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Playlist customerLibrary = customer.getLibrary();
        List<Song> unsavedSongs = songRepo.getUnsavedSongsFromAlbum(customer.getId(), albumId);

        for (Song song: unsavedSongs) {
            PlaylistSong playlistSong = new PlaylistSong(customerLibrary, song);
            playlistSongRepo.save(playlistSong);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Remove an album from the user's library.
     * @param albumId ID of album to remove.
     * @return an empty 200 response on success, otherwise a 404 if the album ID is invalid.
     */
    @DeleteMapping(path = "/albums/{albumId}")
    @Transactional
    public ResponseEntity<?> removeAlbum(@PathVariable Integer albumId) {
        Album album = getAlbumById(albumId);

        if (album == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = getCurrentCustomer();

        playlistSongRepo.deleteAllByPlaylistAndSong_Album(customer.getLibrary(), album);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Return all artists in the user's library.
     * @param page Page index.
     * @return a 200 response containing the set of artists in the user's library.
     */
    @GetMapping(path = "/artists")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getArtists(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = getCurrentCustomer();

        Page<Artist> results = artistRepo.getSavedByCustomerId(customer.getId(), PageRequest.of(page, ITEMS_PER_PAGE));
        List<Artist> artists = new ArrayList<>();

        for (Artist artist: results) {
            artists.add(artist);
        }
        return new TypedCollection(artists, Artist.class);
    }

    private Album getAlbumById(Integer Id){
        Optional<Album> optionalAlbum = albumRepo.findById(Id);
        if (!optionalAlbum.isPresent()){
            return null;
        }
        return optionalAlbum.get();
    }

    private Customer getCurrentCustomer() {
        return (Customer) authFacade.getCurrentUser();
    }
}
