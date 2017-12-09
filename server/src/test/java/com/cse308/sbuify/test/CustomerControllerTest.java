package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.FollowedArtistRepository;
import com.cse308.sbuify.customer.FollowedCustomerRepository;
import com.cse308.sbuify.customer.preferences.Language;
import com.cse308.sbuify.customer.preferences.PreferenceService;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class CustomerControllerTest extends AuthenticatedTest {

    private static final Integer FOLLOWED_CUSTOMER_ID = 1;
    private static final Integer FOLLOWED_ARTIST_ID = 1;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PreferenceService prefService;

    @Autowired
    private FollowedArtistRepository followedArtistRepo;

    @Autowired
    private FollowedCustomerRepository followedCustomerRepo;

    @Test
    @Transactional
    public void follow() {
        ResponseEntity<Void> response;

        // start with a clean slate
        followedArtistRepo.deleteByCustomerAndArtist_Id(user, FOLLOWED_ARTIST_ID);
        followedCustomerRepo.deleteByCustomerAndFriend_Id(user, FOLLOWED_CUSTOMER_ID);

        // test: following customers
        Map<String, Object> params = new HashMap<>();

        params.put("type", "customer");
        params.put("id", FOLLOWED_CUSTOMER_ID);

        response = restTemplate.exchange("/api/customer/following?type={type}&id={id}", HttpMethod.PUT,
                null, Void.class, params);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        params.clear();

        // test: following artists
        params.put("type", "artist");
        params.put("id", FOLLOWED_ARTIST_ID);

        response = restTemplate.exchange("/api/customer/following?type={type}&id={id}", HttpMethod.PUT,
                null, Void.class, params);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Transactional
    public void unfollow() {
        ResponseEntity<Void> response;

        follow();

        // check: can unfollow customer
        Map<String, Object> params = new HashMap<>();

        params.put("type", "customer");
        params.put("id", FOLLOWED_CUSTOMER_ID);

        response = restTemplate.exchange("/api/customer/following?type={type}&id={id}", HttpMethod.DELETE,
                null, Void.class, params);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        params.clear();

        // check: can unfollow artist
        params.put("type", "artist");
        params.put("id", FOLLOWED_ARTIST_ID);

        response = restTemplate.exchange("/api/customer/following?type={type}&id={id}", HttpMethod.DELETE,
                null, Void.class, params);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Transactional
    public void isFollowing() {
        String requestURI = "http://localhost:" + port + "/api/customer/following/contains?type={type}&id={id}";
        Map<String, Object> requestParams = new HashMap<>();
        ResponseEntity<String> response;

        follow();

        // check: response is 'true' when entity is followed
        requestParams.put("type", "artist");
        requestParams.put("id", FOLLOWED_ARTIST_ID);
        response = restTemplate.getForEntity(requestURI, String.class, requestParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody());

        requestParams.clear();

        requestParams.put("type", "customer");
        requestParams.put("id", FOLLOWED_CUSTOMER_ID);
        response = restTemplate.getForEntity(requestURI, String.class, requestParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("true", response.getBody());

        // check: response is 'false' when entity is not followed
        requestParams.put("type", "artist");
        requestParams.put("id", FOLLOWED_ARTIST_ID + 1);
        response = restTemplate.getForEntity(requestURI, String.class, requestParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("false", response.getBody());
        requestParams.clear();
    }

    public void getFollowedPlaylists() {
        // todo: test following/unfollowing playlists
    }

    @Test
    @Transactional
    public void getFollowedArtists() {
        follow();

        // get followed artists
        ResponseEntity<Set<Artist>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/customer/artists",
                        HttpMethod.GET, null, new ParameterizedTypeReference<Set<Artist>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check: expected artists in response?
        Set<Artist> followedArtists = response.getBody();
        for (Artist a: followedArtists) {
            if (FOLLOWED_ARTIST_ID == a.getId())
                return;
        }
        fail("Artist " + FOLLOWED_ARTIST_ID + " is not being followed.");
    }

    @Test
    @Transactional
    public void getFriends() {
        follow();

        // get friends
        ResponseEntity<Set<Customer>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/customer/friends",
                        HttpMethod.GET, null, new ParameterizedTypeReference<Set<Customer>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check: expected friends in response?
        Set<Customer> friends = response.getBody();
        for (Customer c: friends) {
            if (FOLLOWED_CUSTOMER_ID == c.getId())
                return;
        }
        fail("Customer " + FOLLOWED_CUSTOMER_ID + " is not a friend.");
    }

    /**
     * Get Customer preferences.
     */
    @Test
    public void getPreferences() {
        Customer customer = (Customer) user;

        ResponseEntity<Map<String, String>> response =
                restTemplate.exchange("/api/customer/preferences", HttpMethod.GET, null,
                                      new ParameterizedTypeReference<Map<String, String>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, String> responseMap = response.getBody();
        Map<String, String> customerMap = prefService.getAll(customer);

        assertEquals(customerMap, responseMap);
    }

    /**
     * Bulk update customer preferences.
     */
    @Test
    public void updateAllPreferences() {
        Map<String, String> newPref = new HashMap<>();

        // preference values must be valid JSON -- quote them
        newPref.put("hq_streaming", "\"true\"");
        newPref.put("language", "\"SPANISH\"");
        newPref.put("shuffle", "\"false\"");
        newPref.put("repeat", "\"NONE\"");

        // send the request
        HttpEntity<Map<String, String>> request = new HttpEntity<>(newPref);
        ResponseEntity<Void> response =
                restTemplate.exchange("/api/customer/preferences", HttpMethod.PUT, request, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Customer customer = (Customer) user;
        Map<String, String> customerPref = prefService.getAll(customer);

        assertEquals(newPref, customerPref);
    }

    /**
     * Update an individual customer preference.
     */
    @Test
    public void updatePreferenceKey() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "language");

        Customer customer = (Customer) user;

        Language current = prefService.get(customer, "language", Language.class);
        assertNotNull(current);

        Language newLang;
        if (current.equals(Language.ENGLISH)) {
            newLang = Language.SPANISH;
        } else{
            newLang = Language.ENGLISH;
        }
        HttpEntity<Language> request = new HttpEntity<>(newLang);
        ResponseEntity<?> response =
                restTemplate.exchange("/api/customer/preferences/{key}", HttpMethod.PUT, request, Void.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Language language = prefService.get(customer, "language", Language.class);
        assertEquals(newLang, language);
    }

    /**
     * Test: changing the customer's profile picture.
     */
    @Test
    public void changeImage() {
        // todo
    }

    public Album getAlbumById(Integer id){
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        assertTrue(optionalAlbum.isPresent());
        return optionalAlbum.get();
    }

    private Artist getArtistById(Integer id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        assertTrue(optionalArtist.isPresent());
        return optionalArtist.get();
    }

    private Customer getCustomerById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        assertTrue(optionalUser.isPresent());
        return (Customer) optionalUser.get();
    }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}