package com.cse308.sbuify.customer;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistRepository;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/api/customer/recently-played")
public class RecentlyPlayedController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    private final Integer ITEMS_PER_PAGE;

    @Autowired
    public RecentlyPlayedController(LibraryProperties properties) {
        ITEMS_PER_PAGE = properties.getItemPerPage();
    }

    /**
     * Get a list of recently played songs.
     * @param page Page index.
     * @return a 200 response containing a list of songs on success.
     */
    @GetMapping
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getSongs(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Page<Song> songs = songRepository.getRecentlyPlayedByCustomer(customer.getId(),
                PageRequest.of(page, ITEMS_PER_PAGE));
        List<Song> songsList = new ArrayList<>();
        for (Song song: songs) {
            songsList.add(song);
        }
        return new TypedCollection(songsList, Song.class);
    }

    /**
     * Get a list of recently played albums.
     * @param page Page index.
     * @return a 200 response containing a list of albums on success.
     */
    @GetMapping(path = "/albums")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getAlbums(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Page<Album> albums = albumRepository.getRecentlyPlayedByCustomer(customer.getId(),
                PageRequest.of(page, ITEMS_PER_PAGE));
        List<Album> albumList = new ArrayList<>();
        for (Album album: albums) {
            albumList.add(album);
        }
        return new TypedCollection(albumList, Album.class);
    }

    /**
     * Get a list of recently played artists.
     * @param page Page index.
     * @return a 200 response containing a list of artists on success.
     */
    @GetMapping(path = "/artists")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getArtists(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Page<Artist> artists = artistRepository.getRecentlyPlayedByCustomer(customer.getId(),
                PageRequest.of(page, ITEMS_PER_PAGE));
        List<Artist> artistList = new ArrayList<>();
        for (Artist artist: artists) {
            artistList.add(artist);
        }
        return new TypedCollection(artistList, Artist.class);
    }

    /**
     * Get a list of recently played playlists.
     * @param page Page index.
     * @return a 200 response containing a list of artists on success.
     */
    @GetMapping(path = "/playlists")
    @DecorateResponse(type = TypedCollection.class)
    public @ResponseBody TypedCollection getPlaylists(@RequestParam(defaultValue = "0") Integer page) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        Page<Playlist> playlists = playlistRepository.getRecentlyPlayedByCustomer(customer.getId(),
                PageRequest.of(page, ITEMS_PER_PAGE));
        List<Playlist> playlistList = new ArrayList<>();
        for (Playlist playlist: playlists) {
            playlistList.add(playlist);
        }
        return new TypedCollection(playlistList, Playlist.class);
    }
}
