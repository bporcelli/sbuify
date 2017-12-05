package com.cse308.sbuify.test;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.artist.Biography;
import com.cse308.sbuify.artist.BiographyRepository;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArtistControllerTest extends AuthenticatedTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private BiographyRepository biographyRepository;

    /**
     * Test serialization/deserialization of artists.
     * 
     * @throws IOException
     */
    @Test
    public void sedeArtist() throws IOException {
        Artist artist = getArtistById(1);

        String result = new ObjectMapper().writeValueAsString(artist);
        Artist newArtist = new ObjectMapper().readerFor(Artist.class).readValue(result);

        assertEquals(artist, newArtist);
    }

    /**
     * Test serialization/deserialization of artist biographies.
     *
     * @throws IOException
     */
    @Test
    public void sedeBio() throws IOException {
        Biography bio = new Biography();
        bio.setId(1);
        
        List<Image> images = new ArrayList<>();
        bio.setImages(images);
        
        bio.setText("");

        String result = new ObjectMapper().writeValueAsString(bio);

        Biography newBio = new ObjectMapper().readerFor(Biography.class).readValue(result);

        assertEquals(bio, newBio);
    }

    @Test
    public void getArtistInfoTest() {
        ResponseEntity<Artist> response = restTemplate.getForEntity("http://localhost:" + port + "/api/artists/" + 1,
                Artist.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist actual = response.getBody();
        Artist expected = getArtistById(1);

        assertEquals(expected, actual);
    }
    //todo: change to paginable
    @Test
    public void getRelatedArtist(){
        Map<String, String> params = new HashMap<>();
        params.put("artistId", "1");

        ResponseEntity<Set<Artist>> response =
                restTemplate.exchange("/api/artists/{artistId}/related", HttpMethod.GET ,null , new ParameterizedTypeReference<Set<Artist>>() {}, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateArtistBio(){

        Biography mockBio = new Biography();
        mockBio.setText("THIS IS A NEW BIO");
        Artist newBioArtist = new Artist();
        newBioArtist.setBio(mockBio);
        Map<String, String> params = new HashMap<>();
        params.put("artistId", "1");
        HttpEntity<Artist> artist = new HttpEntity<>(newBioArtist);
        ResponseEntity<Void> response =
                restTemplate.exchange("/api/label/artists/{artistId}", HttpMethod.PATCH, artist, Void.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist dbArtist = artistRepository.findById(1).get();

        assertEquals(dbArtist.getBio(), mockBio);

    }


    private Artist getArtistById(Integer id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        assertTrue(optionalArtist.isPresent());
        return optionalArtist.get();
    }


    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
