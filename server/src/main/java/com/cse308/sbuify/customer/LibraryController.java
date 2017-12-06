package com.cse308.sbuify.customer;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
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

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/customer/library")
public class LibraryController {

    // todo: avoid grabbing entire library at once; should attempt to add song twice really be a 400?; pagination?

    @Autowired
    private PlaylistRepository playlistRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AlbumRepository albumRepo;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private ArtistRepository artistRepo;

    /**
     * Get the songs in the customer's library.
     * @return a 200 response with a list of songs in the body.
     */
    @GetMapping("/songs")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getSongs() {
        Customer customer = getCurrentCustomer();
        Playlist library = customer.getLibrary();
        return new TypedCollection(library.getSongs(), PlaylistSong.class);
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

        Playlist lib = customer.getLibrary();
        // song already in customer library
        if(containsSong(lib.getSongs(), id)){
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
    @DeleteMapping(path = "/songs/{id}")
    public ResponseEntity<?> removeSong(@PathVariable Integer id) {
        Customer customer = getCurrentCustomer();
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

    /**
     * Return all albums in the user's library.
     * @return a 200 response containing a list of artists on success.
     */
    @GetMapping(path = "/albums")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getAlbums() {
        Customer customer = getCurrentCustomer();
        List<Album> albums = albumRepo.getSavedByCustomerId(customer.getId());
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
            customerLibrary.add(song);
        }
        playlistRepo.save(customerLibrary);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Remove an album from the user's library.
     * @param albumId ID of album to remove.
     * @return an empty 200 response on success, otherwise a 404 if the album ID is invalid.
     */
    @DeleteMapping(path = "/albums/{albumId}")
    public ResponseEntity<?> removeAlbum(@PathVariable Integer albumId) {
        Album album = getAlbumById(albumId);

        if (album == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = getCurrentCustomer();
        Playlist customerLibrary = customer.getLibrary();
        Collection<Song> albumSongs = album.getSongs();

        for (Song song: albumSongs) {
            customerLibrary.remove(song);
        }
        playlistRepo.save(customerLibrary);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Return all artists in the user's library.
     * @return a 200 response containing the set of artists in the user's library.
     */
    @GetMapping(path = "/artists")
    public @ResponseBody TypedCollection getArtists(){
        Customer customer = getCurrentCustomer();
        List<Artist> savedArtists = artistRepo.getSavedByCustomerId(customer.getId());
        return new TypedCollection(savedArtists, Artist.class);
    }

    protected static boolean containsSong(List<PlaylistSong> list, Integer songId){
        return list.stream().anyMatch(object -> ((Song) object.getSong()).getId().equals(songId));
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
