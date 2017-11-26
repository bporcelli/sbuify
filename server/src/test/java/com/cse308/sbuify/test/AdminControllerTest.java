package com.cse308.sbuify.test;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminControllerTest extends AuthenticatedTest {

    @Autowired
    private SongRepository songRepository;

    /**
     *  Test to get all songs from endpoint admin endpoint /api/songs/
     */
    @Test
    public void getAllSongs(){

        ResponseEntity<List<Song>> res = restTemplate.exchange("http://localhost:" + port + "/api/songs",  HttpMethod.GET,null, new ParameterizedTypeReference<List<Song>>() {
        });

        assertEquals(HttpStatus.OK, res.getStatusCode());

        List<Song> songs = res.getBody();

        assertNotNull(songs);

        Iterable<Song> allSongs = songRepository.findAll();

        List<Song> allSongRepo = new ArrayList<>();

        for (Song song : allSongs){
            allSongRepo.add(song);
        }

        assertEquals(allSongRepo.size(), songs.size());

    }

    /**
     *  Test to activate / deactivate a song.
     */
    @Test
    public void deactivateActivateSong(){

        Map<String, String> params = new HashMap<>();

        params.put("id", "1");

        Song dbSong = songRepository.findById(1).get();

        ResponseEntity<Song> res = null;

        res = restTemplate.exchange("http://localhost:" + port + "/api/songs/{id}",  HttpMethod.PATCH, res, Song.class ,params );

        assertEquals(HttpStatus.OK, res.getStatusCode());

        Song dbSongUpdate = res.getBody();

        // If dbSong activated, request should deactivate and vise versa

        assertEquals(!dbSong.isActive(), dbSongUpdate.isActive());

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
