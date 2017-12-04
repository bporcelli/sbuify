package com.cse308.sbuify.customer;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.playlist.PlaylistSong;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.cse308.sbuify.customer.LibraryController.containsId;

@Controller
@RequestMapping(path = "/api/customer/")
public class CustomerController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private StorageService storageService;

    // todo: refactor to avoid eager fetching of customer's friends, artists, and playlists

    /**
     * Get the playlists followed by the customer.
     *
     * @return Set<Playlist>
     */
    @GetMapping(path = "playlists")
    public ResponseEntity<?> getFollowedPlaylists() {
        Customer customer = getCurrentCustomer();
        TypedCollection collection = new TypedCollection(customer.getPlaylists(), Playlist.class);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    /**
     * Get the artists followed by the customer.
     *
     * @return Set<Artist>
     */
    @GetMapping(path = "artists")
    public ResponseEntity<?> getFollowedArtists() {
        Customer customer = getCurrentCustomer();
        TypedCollection collection = new TypedCollection(customer.getArtists(), Artist.class);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    /**
     * Add the artist followed by the customer.
     * @param artist
     * @return HTTP.OK successful, HTTP.BAD_REQUEST unsuccessful
     */
    @PostMapping( path = "artists")
    public ResponseEntity<?> addArtist(@RequestBody Artist artist){
        Customer customer = getCurrentCustomer();
        Set<Artist> artists = customer.getArtists();
        if (!artists.add(artist)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * delete the artist followed by the customer.
     * @param artistId
     * @return HTTP.OK successful, HTTP.BAD_REQUEST unsuccessful
     */
    @DeleteMapping( path = "artists/{artistId}")
    public ResponseEntity<?> deleteArtist(@PathVariable Integer artistId) throws Exception{
        Customer customer = getCurrentCustomer();
        Set<Artist> artists = customer.getArtists();

        if (!artists.removeIf(artist -> artist.getId().equals(artistId))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get the customer's friends.
     *
     * @return Set<Customer>
     */
    @GetMapping(path = "friends")
    public ResponseEntity<?> getFriends() {
        Customer customer = getCurrentCustomer();
        TypedCollection collection = new TypedCollection(customer.getFriends(), Customer.class);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    /**
     * Follow a playlist, artist, or customer.
     *
     * @param followable The playlist, artist, or customer to follow.
     */
    @PutMapping(path = "following")
    public ResponseEntity<?> follow(@RequestBody Followable followable) {
        return followOrUnfollow(followable, true);
    }

    /**
     * Unfollow a playlist, artist, or customer.
     *
     * @param followable The playlist, artist, or customer to unfollow.
     */
    @DeleteMapping(path = "following")
    public ResponseEntity<?> unfollow(@RequestBody Followable followable) {
        return followOrUnfollow(followable, false);
    }

    /** Helper: follow or unfollow a Followable. */
    private ResponseEntity<?> followOrUnfollow(Followable followable, boolean follow) {
        Customer customer = getCurrentCustomer();
        if (follow) {
            customer.follow(followable);
        } else {
            customer.unfollow(followable);
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Check if the customer followers a playlist, artist, or customer.
     *
     * @param type Entity type -- playlist, artist, or customer.
     * @param id Entity ID.
     * @return 'true' or 'false.'
     */
    @GetMapping(path = "following/contains")
    public ResponseEntity<?> isFollowing(@RequestParam String type, @RequestParam Integer id) {
        Customer customer = getCurrentCustomer();
        Followable followable = null;

        if (type.equals("playlist")) {
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
            if (optionalPlaylist.isPresent())
                followable = optionalPlaylist.get();
        } else if (type.equals("artist")) {
            Optional<Artist> optionalArtist = artistRepository.findById(id);
            if (optionalArtist.isPresent())
                followable = optionalArtist.get();
        } else if (type.equals("customer")) {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent())
                followable = (Customer) optionalUser.get();
        } else {  // entity type invalid
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (followable == null) {  // entity id invalid
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer.isFollowing(followable).toString(), HttpStatus.OK);
    }

    /**
     * Return all unique artist from library
     * @return HashSet of all artist from library playlist with HTTP.OK
     */
    @GetMapping( path = "library/artists")
    public ResponseEntity<?> getArtists(){
        Customer customer = getCurrentCustomer();
        Playlist library = customer.getLibrary();

        List<PlaylistSong> librarySongs = library.getSongs();
        Set<Artist> libraryArtist = new HashSet<>();

        for (PlaylistSong song: librarySongs){
            Artist songArtist = song.getSong().getAlbum().getArtist();
            libraryArtist.add(songArtist);
        }

        TypedCollection artist = new TypedCollection(libraryArtist, Artist.class);

        return new ResponseEntity<>(artist, HttpStatus.OK);

    }

    /**
     * Return all unique albumns from library
     * @return
     */
    @GetMapping( path = "library/albums")
    public ResponseEntity<?> getAlbums(){
        Customer customer = getCurrentCustomer();
        Playlist library = customer.getLibrary();

        List<PlaylistSong> librarySongs = library.getSongs();
        Set<Album> libraryAlbums = new HashSet<>();

        for (PlaylistSong song: librarySongs){
            Album album = song.getSong().getAlbum();
            libraryAlbums.add(album);
        }

        TypedCollection artist = new TypedCollection(libraryAlbums, Album.class);

        return new ResponseEntity<>(artist, HttpStatus.OK);

    }

    /**
     * add an album to library
     * @return Http.OK when successful
     */
    @PostMapping( path = "library/albums")
    public ResponseEntity<?> addAlbum(@RequestBody Album album) {
        Customer customer = getCurrentCustomer();

        Playlist customerLibrary = customer.getLibrary();

        List<PlaylistSong> playlistSongs = customerLibrary.getSongs();

        Collection<Song> albumSongs = album.getItems();
        // customer library only contains unique songs no duplicates
        for( Song song : albumSongs){
            // use containsId to see if library contains that song already
            if (!containsId(playlistSongs, song.getId())){
                customerLibrary.add(song);
            }
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * remove an album to library
     * @return Http.OK when successful, otherwise Http.BAD_REQUEST
     */
    @DeleteMapping( path = "library/albums/{albumId}")
    public ResponseEntity<?> removeAlbum(@PathVariable Integer albumId) {


        Album album = getAlbumById(albumId);

        Collection<Song> albumSongs = album.getSongs();
        if (albumSongs == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Customer customer = getCurrentCustomer();
        Playlist customerLibrary = customer.getLibrary();

        for( Song song : albumSongs){
            customerLibrary.remove(song);
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    /**
     * Change user profile picture
     * @param imageData
     * @return Http.OK when successful, otherwise, Http.BAD_REQUEST
     */
    @PutMapping( path = "profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestBody String imageData){
        Customer customer = getCurrentCustomer();
        Image image;
        try{
            image = storageService.save(imageData);
        } catch (StorageException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        customer.setProfileImage(image);
        userRepository.save(customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Album getAlbumById(Integer Id){
        Optional<Album> optionalAlbum = albumRepository.findById(Id);
        if (!optionalAlbum.isPresent()){
            return null;
        }
        return optionalAlbum.get();
    }

    private Customer getCurrentCustomer(){
        return (Customer) authFacade.getCurrentUser();
    }
}
