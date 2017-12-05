package com.cse308.sbuify.customer;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

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
     * Follow an artist.
     * @param artist
     * @return HTTP.OK successful, HTTP.BAD_REQUEST unsuccessful
     */
    @PostMapping(path = "artists")
    public ResponseEntity<?> followArtist(@RequestBody Artist artist){
        Customer customer = getCurrentCustomer();
        Set<Artist> artists = customer.getArtists();
        if (!artists.add(artist)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Unfollow an artist.
     * @param artistId
     * @return HTTP.OK successful, HTTP.BAD_REQUEST unsuccessful
     */
    @DeleteMapping(path = "artists/{artistId}")
    public ResponseEntity<?> unfollowArtist(@PathVariable Integer artistId) {
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
     * Change the user's profile picture.
     * @param imageData
     * @return Http.OK when successful, otherwise, Http.BAD_REQUEST
     */
    @PutMapping(path = "profile-picture")
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

    private Customer getCurrentCustomer(){
        return (Customer) authFacade.getCurrentUser();
    }
}
