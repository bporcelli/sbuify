package com.cse308.sbuify.test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;

public class AdminControllerTest extends AuthenticatedTest {

    @Autowired
    private SongRepository songRepository;
    
    /**
     * Test if getting all admin endpoint works
     */
    @Test
    public void getAllAdmins() {
        Map<String, String> params = new HashMap<>();
        ResponseEntity<ArrayList<Admin>> response = restTemplate.exchange("/api/admins/",
                HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Admin>>() {}, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // todo: systematically validate this
        List<Admin> admins = response.getBody();
        for(int i = 0; i < admins.size(); i++) {
            System.out.println(admins.get(i));
        }
    }
    
    /**
     * Test if getting admin by id works. Following is the expected admin object. 
     * {
          "type" : "admin",
          "id" : 4,
          "email" : "sbuify+admin@gmail.com",
          "password" : "$2a$10$UeuOatqlhQnbSeeqvHV.MOIFP3sNdrY204Ab7irDCILaTOQOJKy/y",
          "firstName" : "John",
          "lastName" : "Doe",
          "superAdmin" : false
        }
     */
    @Test
    public void getAdminByIdTest() {
        Map<String, String> params = new HashMap<>();
        params.put("id", Integer.toString(4));
        ResponseEntity<Admin> response = restTemplate.getForEntity("/api/admins/{id}", Admin.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Admin admin = response.getBody();
        assertNotEquals(null, admin);
        assertEquals("sbuify+admin@gmail.com", admin.getEmail());
        assertEquals("John", admin.getFirstName());
        assertEquals("Doe", admin.getLastName());
        assertEquals(false, admin.isSuperAdmin());
    }

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
