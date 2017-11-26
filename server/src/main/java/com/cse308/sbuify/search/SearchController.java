package com.cse308.sbuify.search;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.song.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/search")
public class SearchController {

    @Autowired
    private HibernateSearchService searchService;

    /** Searchable entity types */
    private static final Map<String, Class<?>> types = new HashMap<>();

    static {
        types.put("song", Song.class);
        types.put("artist", Artist.class);
        types.put("album", Album.class);
        types.put("playlist", Playlist.class);
        types.put("label", Label.class);
    }

    /**
     * Search for songs, artists, albums, labels, or playlists that match a string keyword.
     *
     * @param query Search query, including the keyword and optional filters.
     * @param type Item type to search for ('song', 'artist', 'album', 'playlist', or 'label').
     * @param limit Maximum number of results to return (default: 20).
     * @param offset Index of the first result to return (default: 0).
     *
     * @return A list of items matching the search query.
     */
    @GetMapping
    public ResponseEntity<?> search(@RequestParam String query,
                                    @RequestParam String type,
                                    @RequestParam(defaultValue = "20") Integer limit,
                                    @RequestParam(defaultValue = "0") Integer offset) {
        if (!types.containsKey(type)) {  // invalid item type
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Class<?> itemType = types.get(type);
        List<?> results = searchService.search(query, itemType, limit, offset);
        TypedCollection response = new TypedCollection(results, itemType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
