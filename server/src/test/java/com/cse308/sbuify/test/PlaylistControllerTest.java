package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.UserRepository;

public class PlaylistControllerTest extends AuthenticatedTest {

    private final static String ENDPOINT_FORMAT_STRING = "http://localhost:%d/api/playlists";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    /**
     * Test to get playlist by ID
     */
    @Test
    public void getPlaylistByIdTest() {
        Customer customer = (Customer) user;

        Playlist playlist = customer.getLibrary();

        Map<String, String> params = new HashMap<>();
        params.put("id", playlist.getId().toString());
        ResponseEntity<Playlist> response = restTemplate
                .getForEntity("http://localhost:" + port + "/api/playlists/{id}/", Playlist.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(playlist, response.getBody());
    }

    /**
     * Create a playlist
     */
    @Test
    public void createPlaylistTest() {
        Customer customer = (Customer) user;

        List<Playlist> owningPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        int previousSize = owningPlaylists.size();

        String name = "New Playlist";
        String description = "This is for testing. Lets see if it works";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Playlist reqObj = new Playlist(name, customer, null, false, 0);
        reqObj.setDescription(description);
        HttpEntity<Playlist> request = new HttpEntity<>(reqObj, headers);

        ResponseEntity<Void> response = restTemplate.exchange(String.format(ENDPOINT_FORMAT_STRING, port),
                HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        owningPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        assertEquals(1, owningPlaylists.size() - previousSize);

        boolean found = false;
        for (Playlist pl : owningPlaylists) {
            if (name.equals(pl.getName()) && description.equals(pl.getDescription())) {
                found = true;
                break;
            }
        }

        assertEquals(true, found);
    }

    // /**
    // * Test to add Song or album to a playlist
    // */
    // @Test
    // public void addSongToPlaylistTest() {
    // Customer customer = (Customer) userRepository.findByEmail(getEmail()).get();
    //
    // List<Playlist> owningPlaylists =
    // playlistRepository.findAllByOwner_Id(customer.getId());
    // assertEquals(true, owningPlaylists.size() > 0);
    // Playlist target = owningPlaylists.get(0);
    // int previousSize = target.getNumSongs();
    //
    //
    // // adding song starts here
    // Optional<Song> newSong = songRepository.findById(3);
    // assertEquals(true, newSong.isPresent());
    //
    // ResponseEntity<Void> response =
    // restTemplate.postForEntity(String.format(ENDPOINT_FORMAT_STRING + "%d/add",
    // port, target.getId()),
    // newSong.get(), Void.class);
    // System.out.println(String.format(ENDPOINT_FORMAT_STRING + "/%d/add", port,
    // target.getId()));
    //
    // assertEquals(HttpStatus.OK, response.getStatusCode());
    //
    // assertEquals(1, target.getNumSongs() - previousSize);
    // }

    /**
     * Update playlist information
     */
    // @Test
    // public void updatePlaylistTest() {
    // Customer customer = (Customer) userRepository.findByEmail(getEmail()).get();
    //
    // String name = "NewName Playlist";
    // String description = "This is for testing. Lets see if it works";
    //
    // Playlist playlist = customer.getLibrary();
    //
    // PlaylistInfoRequestWrapper reqObj = new PlaylistInfoRequestWrapper();
    // reqObj.setName(name);
    // reqObj.setDescription(description);
    //
    // HttpHeaders headers = new HttpHeaders();
    // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    //
    // HttpEntity<PlaylistInfoRequestWrapper> request = new HttpEntity<>(reqObj,
    // headers);
    //
    // ResponseEntity<Void> response = restTemplate.exchange(
    // String.format(ENDPOINT_FORMAT_STRING + "/%d", port, playlist.getId()),
    // HttpMethod.PATCH, request,
    // Void.class);
    //
    // System.out.println(String.format(ENDPOINT_FORMAT_STRING + "/%d", port,
    // playlist.getId()));
    //
    // assertEquals(HttpStatus.OK, response.getStatusCode());
    //
    // playlist = customer.getLibrary();
    // assertEquals(name, playlist.getName());
    // assertEquals(description, playlist.getDescription());
    // }

    // @Test
    // public void deletePlaylistTest() {
    // Customer customer = (Customer) userRepository.findByEmail(getEmail()).get();
    // }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
