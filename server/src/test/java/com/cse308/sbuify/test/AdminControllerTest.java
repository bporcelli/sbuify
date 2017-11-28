package com.cse308.sbuify.test;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class AdminControllerTest extends AuthenticatedTest {

    @Autowired
    private SongRepository songRepository;

    /**
     * Test to activate / deactivate a song.
     */
    @Test
    public void deactivateActivateSong() {
        // todo: use a different endpoint or update existing endpoint to process ALL updates (not just activate/deactivate)
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");

        Optional<Song> optionalSong = songRepository.findById(1);
        assertTrue(optionalSong.isPresent());
        Song original = optionalSong.get();
        Boolean originalActive = original.isActive();
        original.setActive(!originalActive);

        HttpEntity<Song> req = new HttpEntity<>(original);
        ResponseEntity<Song> res = null;
        res = restTemplate.exchange("/api/songs/{id}",  HttpMethod.PATCH, req, Song.class, params);
        assertEquals(HttpStatus.OK, res.getStatusCode());

        Song updated = res.getBody();

        // If original activated, request should deactivate and vise versa
        assertEquals(!originalActive, updated.isActive());
    }

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";  // use the user sbuify+admin@gmail.com for all tests in this class request require admin role
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
