package com.cse308.sbuify.test;

import com.cse308.sbuify.artist.*;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.stream.Stream;
import com.cse308.sbuify.stream.StreamRepository;
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
import java.time.LocalDateTime;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArtistControllerTest extends AuthenticatedTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private StreamRepository streamRepo;

    private static final String NAME = "test";

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
        ResponseEntity<Artist> response = restTemplate.getForEntity("/api/artists/" + 1, Artist.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist actual = response.getBody();
        Artist expected = getArtistById(1);

        assertEquals(expected, actual);
    }

    @Test
    public void getRelatedArtist(){
        Map<String, Object> params = new HashMap<>();

        params.put("artistId", 1);
        params.put("offset", 0);

        ResponseEntity<Set<Artist>> response =
                restTemplate.exchange("/api/artists/{artistId}/related?offset={offset}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<Set<Artist>>() {}, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void updateArtistBio(){
        //todo
    }

    @Test
    public void updateArtistName(){
        Artist updatedArtist = new Artist();
        updatedArtist.setName(NAME);

        Map<String, String> params = new HashMap<>();
        params.put("artistId", "1");

        HttpEntity<Artist> request = new HttpEntity<>(updatedArtist);
        ResponseEntity<Void> response =
                restTemplate.exchange("/api/artists/{artistId}", HttpMethod.PATCH, request, Void.class, params );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist dbArtist = getArtistById(1);
        assertEquals(NAME, dbArtist.getName());
    }

    @Test
    public void updateArtistAlias(){
        Artist updatedArtist = new Artist();
        Set<String> updatedAlias = new HashSet<>();
        updatedAlias.add(NAME);
        updatedArtist.setAliases(updatedAlias);

        Map<String, String> params = new HashMap<>();
        params.put("artistId", "1");

        HttpEntity<Artist> request = new HttpEntity<>(updatedArtist);
        ResponseEntity<Void> response =
                restTemplate.exchange("/api/artists/{artistId}", HttpMethod.PATCH, request, Void.class, params);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Artist dbArtist = getArtistById(1);

        assertEquals(updatedAlias, dbArtist.getAliases());
    }

    @Test
    public void updateImage(){
        //todo
    }

    @Test
    public void addMerchandise(){
        //todo
    }

    @Test
    public void removeMerchandise(){
        //todo
    }

    @Test
    public void updateMerchandise(){
        //todo
    }

    @Test
    public void removeArtist(){
        //todo
    }

    @Test
    public void updateMonthlyListeners(){
        LocalDateTime timeAfterNthHr = LocalDateTime.now().minusDays(30);
        List<Stream> streamsAfter = streamRepo.getAllByTimeAfter(timeAfterNthHr);
        Map<Integer,Integer> map = new Hashtable<>();
        for(Stream stream: streamsAfter){
            Integer artistId = stream.getSong().getAlbum().getArtist().getId();
            Integer streamCount = map.get(artistId);
            if (streamCount == null){
                streamCount = 1;
            } else {
                streamCount++;
            }
            map.put(artistId, streamCount);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Artist artist = getArtistById(key);
            if (artist == null){
                continue;
            }
            artist.setMonthlyListeners(value);
            artistRepository.save(artist);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            Artist artist = getArtistById(key);
            if (artist == null){
                continue;
            }
            assertEquals(value, artist.getMonthlyListeners());
        }
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
