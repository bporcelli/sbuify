package com.cse308.sbuify.customer;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.playlist.*;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(path = "/api/customer/")
public class CustomerFollowingController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PlaylistFolderRepository playlistFolderRepo;

    @Autowired
    private FollowedArtistRepository followedArtistRepo;

    @Autowired
    private FollowedCustomerRepository followedCustomerRepo;

    @Autowired
    private FollowedPlaylistRepository followedPlaylistRepo;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get a list of playlists and playlist folders owned or followed by the user.
     */
    @GetMapping(path = "playlists")
    public ResponseEntity<?> getPlaylists() {
        Customer customer = (Customer) authFacade.getCurrentUser();

        Map<Integer, PlaylistComponent> componentMap = new TreeMap<>();

        // add folders
        List<PlaylistFolder> folders = playlistFolderRepo.findByOwner(customer);

        for (PlaylistFolder folder: folders) {
            Integer sortKey = folder.getPosition() != null ? folder.getPosition() : folder.getId();
            componentMap.put(sortKey, folder);
        }

        // add playlists
        List<FollowedPlaylist> playlists = followedPlaylistRepo.findByCustomerAndParent(customer, null);

        for (FollowedPlaylist savedPlaylist: playlists) {
            Playlist playlist = savedPlaylist.getPlaylist();
            Integer sortKey = savedPlaylist.getPosition() != null ? savedPlaylist.getPosition() : playlist.getId();
            componentMap.put(sortKey, playlist);
        }

        // convert to list
        List<PlaylistComponent> response = new ArrayList<>();
        response.addAll(componentMap.values());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get the artists followed by the customer.
     *
     * @return Set<Artist>
     */
    @GetMapping(path = "artists")
    public ResponseEntity<?> getFollowedArtists() {
        Customer customer = (Customer) authFacade.getCurrentUser();

        List<FollowedArtist> followedArtists = followedArtistRepo.findAllByCustomer(customer);
        List<Artist> artists = new ArrayList<>();

        followedArtists.stream().forEach(followedArtist -> artists.add(followedArtist.getArtist()));

        return new ResponseEntity<>(new TypedCollection(artists, Artist.class), HttpStatus.OK);
    }

    /**
     * Get the customer's friends.
     *
     * @return Set<Customer>
     */
    @GetMapping(path = "friends")
    public ResponseEntity<?> getFriends() {
        Customer customer = (Customer) authFacade.getCurrentUser();

        List<FollowedCustomer> followedCustomers = followedCustomerRepo.findAllByCustomer(customer);
        List<User> friends = new ArrayList<>();

        followedCustomers.stream().forEach(followedCustomer -> friends.add(followedCustomer.getFriend()));

        return new ResponseEntity<>(new TypedCollection(friends, Customer.class), HttpStatus.OK);
    }

    /**
     * Follow a playlist, artist, or customer.
     * @param type ID type ('artist', 'playlist', or 'customer')
     * @param id Artist, playlist, or customer ID.
     */
    @PutMapping(path = "following")
    public ResponseEntity<?> follow(@RequestParam String type, @RequestParam Integer id) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        boolean followed = false;

        if (type.equals("artist")) {
            Optional<Artist> optionalArtist = artistRepository.findById(id);

            if (optionalArtist.isPresent()) {
                FollowedArtist followedArtist = new FollowedArtist(customer, optionalArtist.get());
                followedArtistRepo.save(followedArtist);
                followed = true;
            }
        } else if (type.equals("playlist")) {
            Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);

            if (optionalPlaylist.isPresent()) {
                FollowedPlaylist followedPlaylist = new FollowedPlaylist(customer, optionalPlaylist.get());
                followedPlaylistRepo.save(followedPlaylist);
                followed = true;
            }
        } else if (type.equals("customer")) {
            Optional<User> optionalUser = userRepository.findById(id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                if (user instanceof Customer) {
                    FollowedCustomer followedCustomer = new FollowedCustomer(customer, (Customer) user);
                    followedCustomerRepo.save(followedCustomer);
                    followed = true;
                }
            }
        }

        if (followed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Unfollow a playlist, artist, or customer.
     * @param type ID type ('playlist', 'artist', or 'customer')
     * @param id The ID of the entity to unfollow.
     */
    @DeleteMapping(path = "following")
    @Transactional
    public ResponseEntity<?> unfollow(@RequestParam String type, @RequestParam Integer id) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        boolean unfollowed = false;

        if (type.equals("artist")) {
            Optional<FollowedArtist> followedArtist = followedArtistRepo.findByCustomerAndArtist_Id(customer, id);

            if (followedArtist.isPresent()) {
                followedArtistRepo.delete(followedArtist.get());
                unfollowed = true;
            }
        } else if (type.equals("customer")) {
            Optional<FollowedCustomer> followedCustomer = followedCustomerRepo.findByCustomerAndFriend_Id(customer, id);

            if (followedCustomer.isPresent()) {
                followedCustomerRepo.delete(followedCustomer.get());
                unfollowed = true;
            }
        } else if (type.equals("playlist")) {
            Optional<FollowedPlaylist> followedPlaylist = followedPlaylistRepo.findByCustomerAndPlaylist_Id(customer, id);

            if (followedPlaylist.isPresent()) {
                followedPlaylistRepo.delete(followedPlaylist.get());
                unfollowed = true;
            }
        }

        if (unfollowed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        Customer customer = (Customer) authFacade.getCurrentUser();

        Boolean isFollowing;

        if (type.equals("playlist")) {
            isFollowing = followedPlaylistRepo.existsByCustomerAndPlaylist_Id(customer, id);
        } else if (type.equals("artist")) {
            isFollowing = followedArtistRepo.existsByCustomerAndArtist_Id(customer, id);
        } else if (type.equals("customer")) {
            isFollowing = followedCustomerRepo.existsByCustomerAndFriend_Id(customer, id);
        } else {  // entity type invalid
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(isFollowing.toString(), HttpStatus.OK);
    }
}
