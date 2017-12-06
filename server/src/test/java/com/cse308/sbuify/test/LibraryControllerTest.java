package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.playlist.PlaylistSong;
import com.cse308.sbuify.playlist.PlaylistSongRepository;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LibraryControllerTest extends AuthenticatedTest {

    // todo: test save/remove song

    private static final Integer SAVED_ARTIST_ID = 1;
    private static final Integer SAVED_SONG_ID = 1;
    private static final int ITEMS_PER_PAGE = 25;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistSongRepository playlistSongRepo;

    /**
     * Get a customer's saved songs.
     */
    @Test
    public void getSavedSongs() {
        Customer customer = (Customer) user;

        ResponseEntity<List<PlaylistSong>> response =
                restTemplate.exchange("/api/customer/library/songs", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<PlaylistSong>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Page<PlaylistSong> result = playlistSongRepo.getLibrarySongs(customer.getId(),
                PageRequest.of(0, ITEMS_PER_PAGE));
        List<PlaylistSong> savedSongs = new ArrayList<>();

        for (PlaylistSong ps: result) {
            savedSongs.add(ps);
        }

        List<PlaylistSong> responseSongs = response.getBody();
        assertEquals(savedSongs, responseSongs);
    }

    /**
     * Get customer's saved artists.
     */
    @Test
    public void getSavedArtists() {
        Customer customer = (Customer) user;
        Playlist library = customer.getLibrary();

        // start with a clean slate
        ArrayList<Song> toRemove = new ArrayList<>();

        for (PlaylistSong s: library.getSongs()) {
            Song song = s.getSong();
            Album album = song.getAlbum();
            Artist artist = album.getArtist();
            if (artist.getId() == SAVED_ARTIST_ID) {
                toRemove.add(song);
            }
        }
        toRemove.forEach(song -> library.remove(song));

        // save a song by the artist to the user's library (we should query for these, but i'll cheat)
        library.add(getSongById(SAVED_SONG_ID));
        playlistRepository.save(library);

        // get saved artists
        ResponseEntity<Set<Artist>> response =
                restTemplate.exchange("/api/customer/library/artists", HttpMethod.GET,null,
                        new ParameterizedTypeReference<Set<Artist>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // check: does response contain SAVED_ARTIST_ID
        Set<Artist> responseArtist = response.getBody();
        for (Artist artist: responseArtist) {
            if (artist.getId() == SAVED_ARTIST_ID) {  // success!
                return;
            }
        }
        fail("Artist " + SAVED_ARTIST_ID + " isn't saved in the user's library.");
    }

    /**
     * Get albums saved in the customer's library.
     */
    @Test
    public void getSavedAlbums(){
        Customer customer = (Customer) user;

        Page<Album> firstAlbumPage = albumRepository.getSavedByCustomerId(customer.getId(),
                PageRequest.of(0, ITEMS_PER_PAGE));
        List<Album> libraryAlbums = new ArrayList<>();

        for (Album album: firstAlbumPage) {
            libraryAlbums.add(album);
        }

        ResponseEntity<List<Album>> response =
                restTemplate.exchange("/api/customer/library/albums", HttpMethod.GET,null,
                        new ParameterizedTypeReference<List<Album>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Album> responseAlbum = response.getBody();
        assertEquals(libraryAlbums, responseAlbum);
    }

    /**
     * Save an album to the user's library.
     */
    @Test
    public void saveAlbum() {
        Album album = getAlbumById(4);

        // use album id to keep consistent with song endpoints
        Map<String, String> params = new HashMap<>();
        params.put("id", "4");

        ResponseEntity<?> response = restTemplate.postForEntity(
                "/api/customer/library/albums/{id}", null, Void.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Customer customer = getCustomerById(user.getId());
        Playlist library = customer.getLibrary();

        // check: are all songs on the album saved in the user's library?
        List<Song> librarySongs = library.getSongs()
                .stream()
                .map(playlistSong -> playlistSong.getSong())
                .collect(Collectors.toList());
        Set<Song> albumSongs = album.getSongs();

        for (Song song: albumSongs) {
            if (!librarySongs.contains(song)) {
                fail("Song " + song.getId() + " wasn't saved to the user's library.");
                return;
            }
        }
    }

    /**
     * Remove an album from the user's library.
     */
    @Test
    public void removeAlbum() {
        Album album = getAlbumById(4);

        Map<String, String> params = new HashMap<>();
        params.put("albumId", album.getId().toString());
        ResponseEntity<?> response = restTemplate.exchange(
                "/api/customer/library/albums/{albumId}", HttpMethod.DELETE, null, Void.class, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Customer customer = getCustomerById(user.getId());
        Playlist library = customer.getLibrary();
        List<PlaylistSong> playlistLibrarySong = library.getSongs();

        boolean found = false;
        for (PlaylistSong playlistSong: playlistLibrarySong){
            Song librarySong = playlistSong.getSong();
            if (album.equals(librarySong.getAlbum())){
                found = true;
                break;
            }
        }
        assertEquals(false, found);
    }

    public Album getAlbumById(Integer id){
        Optional<Album> optionalAlbum = albumRepository.findById(id);
        assertTrue(optionalAlbum.isPresent());
        return optionalAlbum.get();
    }

    private Song getSongById(Integer id){
        Optional<Song> optionalSong = songRepository.findById(id);
        assertTrue(optionalSong.isPresent());
        return optionalSong.get();
    }

    private Customer getCustomerById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        assertTrue(optionalUser.isPresent());
        return (Customer) optionalUser.get();
    }

    @Override
    public String getEmail() {
        return "sbuify+c@gmail.com";
    }

    @Override
    public String getPassword() {
        return "c";
    }
}
