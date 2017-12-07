package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.util.StreamUtils;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.image.Base64Image;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.UserRepository;

public class PlaylistControllerTest extends AuthenticatedTest {

    private static final String TEST_IMAGE = "test-image.jpg";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private StorageService storageService;

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
    public void createPlaylistTest() throws IOException {
        Customer customer = (Customer) user;

        // build playlist
        Resource resource = storageService.loadAsResource(TEST_IMAGE);

        InputStream stream = resource.getInputStream();
        byte[] bytes = StreamUtils.copyToByteArray(stream);
        stream.close();

        Base64Image image = new Base64Image();
        image.setDataURL("data:image/jpeg;base64," + Base64Utils.encodeToString(bytes));
        Playlist reqObj = new Playlist( "New Playlist", customer, image, false);
        reqObj.setDescription("This is for testing. Lets see if it works");

        // attempt to create playlist
        List<Playlist> ownedPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        int previousSize = ownedPlaylists.size();

        HttpEntity<Playlist> request = new HttpEntity<>(reqObj);
        ResponseEntity<Playlist> response = restTemplate.postForEntity("/api/playlists", request, Playlist.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check: does new playlist count equal old count + 1?
        ownedPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        assertEquals(previousSize + 1, ownedPlaylists.size());

        // check: is saved playlist in ownedPlaylists?
        assertTrue(ownedPlaylists.contains(response.getBody()));
    }

    /**
     * Test to add Song or album to a playlist
     */
    @Test
    public void addSongToPlaylistTest() {
        Customer customer = (Customer) user;

        List<Playlist> owningPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        assertEquals(true, owningPlaylists.size() > 0);
        Playlist target = owningPlaylists.get(0);
        int previousSize = target.getSongs().size();

        // adding song starts here
        Optional<Song> newSong = songRepository.findById(3);
        assertEquals(true, newSong.isPresent());

        ResponseEntity<Void> response = restTemplate.postForEntity(
                String.format("/api/playlists/%d/add", target.getId()), newSong.get(), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // get the updated version
        owningPlaylists = playlistRepository.findAllByOwner_Id(customer.getId());
        target = owningPlaylists.get(0);

        assertEquals(target.getSongs().size(), previousSize + 1);
    }

    /**
     * Update playlist information
     */
    @Test
    public void updatePlaylistTest() {
        Customer customer = (Customer) user;

        String name = "NewName Playlist";
        String description = "This is for testing. Lets see if it works";

        Playlist reqObj = customer.getLibrary();
        reqObj.setName(name);
        reqObj.setDescription(description);

        HttpEntity<Playlist> request = new HttpEntity<>(reqObj);
        ResponseEntity<Void> response = restTemplate.exchange(
                String.format("/api/playlists/%d", reqObj.getId()), HttpMethod.PATCH, request,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Playlist playlist = customer.getLibrary();
        assertEquals(name, playlist.getName());
        assertEquals(description, playlist.getDescription());
    }

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
