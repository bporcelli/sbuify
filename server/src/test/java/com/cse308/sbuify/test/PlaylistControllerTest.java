package com.cse308.sbuify.test;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PlaylistControllerTest extends AuthenticatedTest {

    @Autowired
    private UserRepository userRepository;

    /**
     * Test to get playlist by ID
     */
    @Test
    public void getPlaylistById() {
        Customer customer = (Customer) userRepository.findByEmail(getEmail()).get();

        Playlist playlist = customer.getLibrary();

        Map<String, String> params = new HashMap<>();
        params.put("id", playlist.getId().toString());
        ResponseEntity<Playlist> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/playlists/{id}", Playlist.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(playlist, response.getBody());
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
