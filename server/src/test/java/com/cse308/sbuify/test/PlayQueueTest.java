package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.customer.PlayQueue;
import com.cse308.sbuify.customer.PlayQueueRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayQueueTest extends AuthenticatedTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlayQueueRepository playQueueRepository;

    @Test
    public void sedeSong() throws IOException {
        Song song = getSongById(1);

        String serializedSong = new ObjectMapper().writeValueAsString(song);
        Queueable newSong = new ObjectMapper().readValue(serializedSong, Queueable.class);

        assertEquals(song, newSong);
    }

    /**
     * Test serialization/deserialization of albums.
     */
    @Test
    public void sedeAlbum() throws IOException {
        Album album = getAlbumById(1);

        String serializedAlbum = new ObjectMapper().writeValueAsString(album);
        Queueable newAlbum = new ObjectMapper().readValue(serializedAlbum, Queueable.class);

        assertEquals(album, newAlbum);
    }

    /**
     * Test serialization/deserialization of PlayQueue.
     */
    @Test
    public void sedePlayQueue() throws IOException {
        Album album = getAlbumById(1);

        PlayQueue pq = new PlayQueue();
        pq.addAll(album.getItems());

        ObjectMapper om = new ObjectMapper();
        String serializedPQ = om.writeValueAsString(pq);
        PlayQueue newPq = om.readValue(serializedPQ, PlayQueue.class);

        assertEquals(pq, newPq);
    }

    /**
     * Test: is search song work properly?
     */
    @Test
    public void putPlayQueue() {
        // get original play queue
        PlayQueue playQueue = getPlayQueueById(1);

        // add some new songs
        playQueue.update(Arrays.asList(getSongById(1), getSongById(2)));

        // send the request
        HttpEntity<PlayQueue> request = new HttpEntity<>(playQueue);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/customer/play-queue",
                HttpMethod.PUT, request, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addToPlayQueue() {
        Song s1 = getSongById(1);

        // send the request
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/customer/play-queue/add", s1, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void rmFromPlayQueue() {
        // add song to queue
        Song s1 = getSongById(1);
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/customer/play-queue/add", s1, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // remove song from queue
        response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/customer/play-queue/remove", s1, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private Album getAlbumById(Integer id) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        assertTrue(optionalAlbum.isPresent());
        return optionalAlbum.get();
    }

    private Song getSongById(Integer id) {
        Optional<Song> optionalSong = songRepository.findById(id);
        assertTrue(optionalSong.isPresent());
        return optionalSong.get();
    }

    private PlayQueue getPlayQueueById(Integer id) {
        Optional<PlayQueue> optionalPlayQueue = playQueueRepository.findById(id);
        assertTrue(optionalPlayQueue.isPresent());
        return optionalPlayQueue.get();
    }

    @Override
    public String getEmail() {
        return "sbuify+a@gmail.com";  // use user "sbuify+a@gmail.com" for all tests in this class
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
