package com.cse308.sbuify.test;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.artist.Biography;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ArtistControllerTest extends AuthenticatedTest {

    @Autowired
    private ArtistRepository artistRepository;

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
    
 // TODO: Use real bio info
//    @Test
//    public void getArtistBioTest() {
//        setRestTemplate(restTemplate);
//
//        ResponseEntity<Biography> response = restTemplate.getForEntity("http://localhost:" + port + "/api/artists/" + 1 + "/bio",
//                Biography.class);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        Biography bio = response.getBody();
//
//        Biography expected = new Biography();
//        expected.setId(1);
//        
//        List<Image> images = new ArrayList<>();
//        images.add(new Image());
//        expected.setImages(images);
//        
//        expected.setText("");
//        
//        assertEquals(expected, bio);
//    }

    private Artist getArtistById(Integer id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        assertTrue(optionalArtist.isPresent());
        return optionalArtist.get();
    }

    @Override
    public String getEmail() {
        return "sbuify+a@gmail.com"; // use user "sbuify+a@gmail.com" for all tests in this class
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
