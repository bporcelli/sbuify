package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.label.LabelRepository;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static com.cse308.sbuify.search.HibernateSearchService.FILTER_SEPARATOR;
import static com.cse308.sbuify.search.HibernateSearchService.TERM_SEPARATOR;
import static org.junit.Assert.*;

public class SearchTest extends AuthenticatedTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private LabelRepository labelRepository;

    /**
     * Test: is search song work properly?
     */
    @Test
    public void searchSongs() {
        Song testSong = getSongById(1);

        // use the name of the song as the search keyword
        String keyword = testSong.getName();

        // execute the search
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword);
        queryParams.put("type", "song");
        ResponseEntity<List<Song>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Song>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect testSong to be the first result
        List<Song> results = response.getBody();
        assertFalse(results.isEmpty());
        assertEquals(testSong, results.get(0));
    }
    
    /**
     * Test: searching for artists with default 'owned' value.
     */
    @Test
    public void searchArtists() {
        Artist testArtist = getArtistById(2);

        // use the name of the artist as the search keyword
        String keyword = testArtist.getName();

        // execute the search
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword);
        queryParams.put("type", "artist");
        ResponseEntity<List<Artist>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Artist>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect first result to be testArtist
        List<Artist> results = response.getBody();
        assertFalse(results.isEmpty());
        assertEquals(testArtist, results.get(0));
    }
    
    /**
     * Test: searching for artists with custom 'owned' value.
     */
    @Test
    public void searchArtistsWithOwned() {
        Artist testArtist = getArtistById(2);

        // use the name of the artist as the search keyword
        String keyword = testArtist.getName();

        // search for UNOWNED entities matching the keyword
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword + TERM_SEPARATOR + "owner" + FILTER_SEPARATOR + "null");
        queryParams.put("type", "artist");
        ResponseEntity<List<Artist>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Artist>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect a non-empty result set (in test data, no artists are owned)
        List<Artist> unownedArtists = response.getBody();
        assertFalse(unownedArtists.isEmpty());
    }
    
    /**
     * Test: searching for albums.
     */
    @Test
    public void searchAlbums() {
        Album testAlbum = getAlbumById(1);

        // use album name as keyword
        String keyword = testAlbum.getName();

        // execute the search
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword);
        queryParams.put("type", "album");
        ResponseEntity<List<Album>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Album>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect first result to be testAlbum
        List<Album> results = response.getBody();
        assertFalse(results.isEmpty());
        assertEquals(testAlbum, results.get(0));
    }
    
    /**
     * Test: searching for playlists.
     */
    @Test
    public void searchPlaylist() {
        Playlist testPlaylist = getPlaylistById(1);

        // use playlist name as keyword
        String keyword = testPlaylist.getName();

        // execute the search
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword);
        queryParams.put("type", "playlist");
        ResponseEntity<List<Playlist>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Playlist>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect first result to be testPlaylist
        List<Playlist> results = response.getBody();
        assertFalse(results.isEmpty());
        assertEquals(testPlaylist, results.get(0));
    }
    
    /**
     * Test: searching record labels.
     */
    @Test
    public void searchLabel() {
        Label testLabel = getLabelById(1);
        assertNull(testLabel.getOwner()); // only unowned labels are returned from the label search endpoint

        // use playlist name as keyword
        String keyword = testLabel.getName();

        // execute the search
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("query", keyword);
        queryParams.put("type", "label");
        ResponseEntity<List<Label>> response =
                restTemplate.exchange("http://localhost:" + port + "/api/search/?query={query}&type={type}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Label>>() {}, queryParams);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // expect first result to be testLabel
        List<Label> results = response.getBody();
        assertFalse(results.isEmpty());
        Label first = results.get(0);
        assertEquals(testLabel, first);
    }

    private Song getSongById(Integer id) {
        Optional<Song> optionalSong = songRepository.findById(id);
        assertTrue(optionalSong.isPresent());
        return optionalSong.get();
    }

    private Artist getArtistById(Integer id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        assertTrue(optionalArtist.isPresent());
        return optionalArtist.get();
    }

    private Album getAlbumById(Integer id) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        assertTrue(optionalAlbum.isPresent());
        return optionalAlbum.get();
    }

    private Playlist getPlaylistById(Integer id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        assertTrue(optionalPlaylist.isPresent());
        return optionalPlaylist.get();
    }

    private Label getLabelById(Integer id) {
        Optional<Label> optionalLabel = labelRepository.findById(id);
        assertTrue(optionalLabel.isPresent());
        return optionalLabel.get();
    }

    @Override
    public String getEmail() {
        return "sbuify+a@gmail.com";  // tests will run with user sbuify+a@gmail.com authenticated
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
