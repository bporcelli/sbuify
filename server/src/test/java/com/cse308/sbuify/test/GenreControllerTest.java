package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.song.Genre;
import com.cse308.sbuify.song.GenreRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class GenreControllerTest extends AuthenticatedTest {

    private static final int TEST_GENRE_ID = 866;  // "Rock"

    private static final int NUM_ALBUMS = 6; // todo: pull from config file

    @Autowired
    private GenreRepository genreRepository;

    /**
     * Test: retrieving a list of all genres.
     */
    @Test
    public void getAll() {
        ResponseEntity<ArrayList<Genre>> response = restTemplate.exchange("/api/genres", HttpMethod.GET,
                null, new ParameterizedTypeReference<ArrayList<Genre>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    /**
     * Test: retrieving a single genre.
     */
    @Test
    public void getOne() {
        Optional<Genre> optionalGenre = genreRepository.findById(TEST_GENRE_ID);
        assertTrue(optionalGenre.isPresent());
        Genre expected = optionalGenre.get();

        Map<String, String> params = new HashMap<>();
        params.put("id", Integer.toString(TEST_GENRE_ID));

        ResponseEntity<Genre> response = restTemplate.getForEntity("/api/genres/{id}", Genre.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    /**
     * Test: getting popular albums.
     */
    @Test
    public void getPopularAlbums() {
        getAlbumsHelper("popular");
    }

    /**
     * Test: getting recent albums.
     */
    @Test
    public void getRecentAlbums() {
        getAlbumsHelper("recent");
    }

    private void getAlbumsHelper(String type) {
        Map<String, String> params = new HashMap<>();
        params.put("id", Integer.toString(TEST_GENRE_ID));
        params.put("type", type);
        ResponseEntity<ArrayList<Album>> response = restTemplate.exchange("/api/genres/{id}/{type}",
                HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Album>>() {}, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(NUM_ALBUMS, response.getBody().size());
    }

    @Override
    public String getEmail() {
        return "sbuify+a@gmail.com";
    }

    @Override
    public String getPassword() {
        return "a";
    }
}