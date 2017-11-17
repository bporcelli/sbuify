package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.PlayQueue;
import com.cse308.sbuify.security.SecurityConstants;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.test.helper.LoginHelper;
import com.cse308.sbuify.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayQueueTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void sedeSong() throws IOException {
        Song song = new Song();

        String result = new ObjectMapper().writeValueAsString(song);

        System.out.println(result);

        Song newSong = new ObjectMapper().readerFor(Queueable.class).readValue(result);

        assertEquals(song, newSong);
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

        System.out.println(result);

        Queueable newAlbum = new ObjectMapper().readerFor(Queueable.class).readValue(result);

        assertEquals(album, newAlbum);
    }

    @Test
    public void sedePlayQueue() throws IOException {

        PlayQueue pq = new PlayQueue();

        List<Queueable> lq = new ArrayList<>();

        Song s1 = new Song();
        s1.setName("Hello!");
        lq.add(s1);

        Album toAdd = new Album();
        toAdd.addSong(new Song());
        toAdd.addSong(new Song());
        lq.add(toAdd);

        pq.addAll(toAdd);

        String result = new ObjectMapper().writeValueAsString(pq);

        System.out.println(result);

        PlayQueue newPq = new ObjectMapper().readValue(result, PlayQueue.class);

        assertEquals(pq, newPq);
    }

    /**
     * Test: is search song work properly?
     */
    @Test
    public void putPlayQueue() {
        // simulate login
        Customer customer = LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port,
                restTemplate);

        // set appropriate headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(SecurityConstants.HEADER_NAME, SecurityConstants.HEADER_PREFIX + customer.getToken());

        // create PlayQueue to send
        PlayQueue toSend = new PlayQueue();

        Song s1 = new Song();
        s1.setName("hello");
        s1.setCreatedDate(LocalDateTime.now());

        Song s2 = new Song();
        s2.setName("world");
        s2.setCreatedDate(LocalDateTime.now());

        Song s3 = new Song();
        s3.setName("album");
        s3.setCreatedDate(LocalDateTime.now());

        toSend.getSongs().add(s1);
        toSend.getSongs().add(s2);
        toSend.getSongs().add(s3);

        // create request object
        HttpEntity<PlayQueue> request = new HttpEntity<>(toSend, headers);

        System.out.println(request);

        // send the request
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/customer/play-queue",
                HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addToPlayQueue() {
        // simulate login
        Customer customer = LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port,
                restTemplate);

        // set appropriate headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(SecurityConstants.HEADER_NAME, SecurityConstants.HEADER_PREFIX + customer.getToken());

        Song s1 = new Song();
        s1.setName("iWannaAddThis");
        s1.setCreatedDate(LocalDateTime.now());

        // create request object
        HttpEntity<Queueable> request = new HttpEntity<>(s1, headers);

        // send the request
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/customer/play-queue/add", HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void rmFromPlayQueue() {
        // simulate login
        Customer customer = LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port,
                restTemplate);

        // set appropriate headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(SecurityConstants.HEADER_NAME, SecurityConstants.HEADER_PREFIX + customer.getToken());

        Song s1 = new Song();
        s1.setName("iWannaAddThis");
        s1.setCreatedDate(LocalDateTime.now());

        // create request object
        HttpEntity<Queueable> request = new HttpEntity<>(s1, headers);

        // send the request
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/customer/play-queue/add", HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // now request to remove
        response = restTemplate.exchange("http://localhost:" + port + "/api/customer/play-queue/remove",
                HttpMethod.POST, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
