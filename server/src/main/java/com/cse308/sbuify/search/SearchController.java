package com.cse308.sbuify.search;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.song.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/api/search")
public class SearchController {

    @Autowired
    private HibernateSearchService searchService;

    // todo: add pagination (see https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/#_pagination)

    /**
     * Search for songs matching a string keyword.
     *
     * @param keyword Search keyword.
     * @return A list of songs matching the search keyword.
     */
    @GetMapping(path = "/songs")
    public @ResponseBody TypedCollection searchSongs(@RequestParam String keyword) {
        List<Song> results = searchService.fuzzySearch(keyword, Song.class);
        return new TypedCollection(results, Song.class);
    }

    /**
     * Search for artists matching the given query criteria.
     *
     * @param keyword Search keyword.
     * @param owned Should only owned artists be returned?
     * @return List of Artist matching the given criteria.
     */
    @GetMapping(path = "/artists")
    public @ResponseBody TypedCollection searchArtists(@RequestParam String keyword,
                                                       @RequestParam(required = false) Boolean owned) {
        List<Artist> results = searchService.fuzzySearch(keyword, owned, Artist.class);
        return new TypedCollection(results, Artist.class);
    }

    /**
     * Search for albums matching the given search keyword.
     *
     * @param keyword Search keyword.
     * @return List of Album matching the search keyword.
     */
    @GetMapping(path = "/albums")
    public @ResponseBody TypedCollection searchAlbums(@RequestParam String keyword) {
        List<Album> results = searchService.fuzzySearch(keyword, Album.class);
        return new TypedCollection(results, Album.class);
    }

    /**
     * Search for playlists matching the given search keyword.
     *
     * @param keyword Search keyword.
     * @return
     */
    @GetMapping(path = "/playlists")
    public @ResponseBody TypedCollection searchPlaylists(@RequestParam String keyword) {
        // todo: filter out hidden playlists unless they are owned by the current user.
        List<Playlist> results = searchService.fuzzySearch(keyword, Playlist.class);
        return new TypedCollection(results, Playlist.class);
    }

    /**
     * Search for orphaned labels matching the given search keyword.
     *
     * @param keyword Search keyword.
     * @return List of orphaned (null owner) Label matching the search keyword.
     */
    @GetMapping(path = "/labels")
    public @ResponseBody TypedCollection searchLabels(@RequestParam String keyword) {
        List<Label> results = searchService.fuzzySearch(keyword, false, Label.class);
        return new TypedCollection(results, Label.class);
    }
}
