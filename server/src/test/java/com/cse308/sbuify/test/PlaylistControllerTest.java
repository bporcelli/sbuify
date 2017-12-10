package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.cse308.sbuify.playlist.OverviewPlaylist;
import com.cse308.sbuify.playlist.PlaylistSongRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
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

public class PlaylistControllerTest extends AuthenticatedTest {

    private static final String TEST_IMAGE = "static/img/test-image.jpg";

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepo;

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
        int previousSize = playlistSongRepo.countAllByPlaylist(target);

        // adding song starts here
        Optional<Song> newSong = songRepository.findById(3);
        assertEquals(true, newSong.isPresent());

        ResponseEntity<Void> response = restTemplate.postForEntity(
                String.format("/api/playlists/%d/add", target.getId()), newSong.get(), Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // get updated song count
        int newSize = playlistSongRepo.countAllByPlaylist(target);

        assertEquals(previousSize + 1, newSize);
    }

    /**
     * Update playlist information
     */
    @Test
    public void updatePlaylistTest() {
        Customer customer = (Customer) user;

        Playlist reqObj = customer.getLibrary();

        Playlist updated = new Playlist();

        updated.setName("NewName Playlist");
        updated.setDescription("This is for testing. Lets see if it works");

        HttpEntity<Playlist> request = new HttpEntity<>(updated);
        ResponseEntity<Void> response = restTemplate.exchange(
                String.format("/api/playlists/%d", reqObj.getId()), HttpMethod.PATCH, request,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Playlist playlist = customer.getLibrary();
        assertEquals(updated.getName(), playlist.getName());
        assertEquals(updated.getDescription(), playlist.getDescription());
    }

    @Test
    public void getOverviewPlaylist(){
        ResponseEntity<ArrayList<OverviewPlaylist>> response = restTemplate.exchange("/api/playlists/overview",
                HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<OverviewPlaylist>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ArrayList<OverviewPlaylist> overviewPlaylistsRes = response.getBody();
        // 7 is fixed number
        assertEquals(7 ,overviewPlaylistsRes.size());
    }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
