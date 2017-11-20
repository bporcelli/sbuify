package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.Biography;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtistControllerTest extends AuthenticatedTest {

    // @LocalServerPort
    // private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Serialize and desericalization of artist
     * 
     * @throws IOException
     */

    @Test
    public void sedeArtist() throws IOException {
        Artist artist = new Artist();

        artist.setId(1);
        artist.setName("ArtistName1");

        Set<String> aliases = new HashSet<>();
        aliases.add("Great Artist");
        aliases.add("ArtistName2");

        artist.setAliases(aliases);

        artist.setCreatedDate(LocalDateTime.now());

        artist.setMBID("asdfasdf");

        System.out.println(artist);

        String result = new ObjectMapper().writeValueAsString(artist);

        System.out.println(result);

        Artist newArtist = new ObjectMapper().readerFor(CatalogItem.class).readValue(result);

        assertEquals(artist, newArtist);
    }

    @Test
    public void sedeAlbum() throws IOException {
        Album album = new Album();

        Song s1 = new Song();
        s1.setName("hello");

        Song s2 = new Song();
        s1.setName("world");

        Song s3 = new Song();
        s1.setName("album");

        album.addSong(s1);
        album.addSong(s2);
        album.addSong(s3);

        String result = new ObjectMapper().writeValueAsString(album);

        CatalogItem newAlbum = new ObjectMapper().readerFor(CatalogItem.class).readValue(result);

        assertEquals(album, newAlbum);
    }
    
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
        setRestTemplate(restTemplate);

        ResponseEntity<Artist> response = restTemplate.getForEntity("http://localhost:" + port + "/api/artists/" + 1,
                Artist.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist artist = response.getBody();

        Artist expected = new Artist();
        
        expected.setId(1);
        expected.setName("John Denver");
        expected.setCreatedDate(LocalDateTime.of(2017, 11, 19, 21, 19, 3));
        expected.setMBID("34e10b51-b5c6-4bc1-b70e-f05f141eda1e");
        
        Set<String> aliases = new HashSet<>();
        aliases.add("John Dennver");
        aliases.add("Denver, John");
        expected.setAliases(aliases);

        assertEquals(expected, artist);
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

    @Override
    public String getEmail() {
        return "a@sbuify.com"; // use user "a@sbuify.com" for all tests in this class
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
