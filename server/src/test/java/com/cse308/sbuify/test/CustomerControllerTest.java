package com.cse308.sbuify.test;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.preferences.Language;
import com.cse308.sbuify.customer.preferences.PreferenceService;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistSong;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

public class CustomerControllerTest extends AuthenticatedTest {

    private static final Integer FOLLOWED_CUSTOMER_ID = 1;
    private static final Integer FOLLOWED_ARTIST_ID = 1;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PreferenceService prefService;

    @Test
    public void follow() {
        Followable toFollow;
        RequestEntity<Followable> request;
        ResponseEntity<Void> response;
        URI requestURI;

        try {
            requestURI = new URI("http://localhost:" + port + "/api/customer/following");
        } catch (URISyntaxException e) {
            fail();
            return;
        }

        // test: following customers
        toFollow = getCustomerById(FOLLOWED_CUSTOMER_ID);
        request = RequestEntity.put(requestURI).body(toFollow, Followable.class);
        response = restTemplate.exchange(request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // test: following artists
        toFollow = getArtistById(FOLLOWED_ARTIST_ID);
        request = RequestEntity.put(requestURI).body(toFollow, Followable.class);
        response = restTemplate.exchange(request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void unfollow() {
        Followable toUnfollow;
        HttpEntity<Followable> request;
        ResponseEntity<Void> response;
        URI requestURI;

        try {
            requestURI = new URI("http://localhost:" + port + "/api/customer/following");
        } catch (URISyntaxException e) {
            fail();
            return;
        }

        follow();

        // check: can unfollow customer
        toUnfollow = getCustomerById(FOLLOWED_CUSTOMER_ID);
        request = new HttpEntity<>(toUnfollow);
        response = restTemplate.exchange(requestURI, HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // check: can unfollow artist
        toUnfollow = getArtistById(FOLLOWED_ARTIST_ID);
        request = new HttpEntity<>(toUnfollow);
        response = restTemplate.exchange(requestURI, HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
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
     *  Get Customer Library test
     */
    @Test
    public void getLibrary() {
        Customer customer = (Customer) user;

        ResponseEntity<List<PlaylistSong>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/customer/songs", HttpMethod.GET, null, new ParameterizedTypeReference<List<PlaylistSong>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Playlist customerLibrary = customer.getLibrary();
        // Returns a Persistent Bag
        List<PlaylistSong> customerPlaylist = customerLibrary.getSongs();
        // Convert response to arraylist
        List<PlaylistSong> customerArrayList = new ArrayList<>();
        customerArrayList.addAll(customerPlaylist);

        List<PlaylistSong> responsePlaylist = response.getBody();

        assertEquals(customerArrayList, responsePlaylist);
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