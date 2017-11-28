package com.cse308.sbuify.song;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.common.TypedCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/genres")
public class GenreController {

    private final static int NUM_ALBUMS = 6;  // todo: make configurable with @ConfigProperties

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private AlbumRepository albumRepository;

    /**
     * Get a list of all genres.
     * @return a 200 response with a list of genres in the body.
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public @ResponseBody ArrayList<Genre> getAll() {
        Iterable<Genre> genreIterator = genreRepository.findAll();
        ArrayList<Genre> genres = new ArrayList<>();
        for (Genre genre: genreIterator) {
            genres.add(genre);
        }
        return genres;
    }

    /**
     * Get information about a genre.
     * @param id Genre ID.
     * @return a 200 response with a genre in the body, otherwise a 404 response if the provided id is invalid.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);
        if (!optionalGenre.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalGenre.get(), HttpStatus.OK);
    }

    /**
     * Get NUM_ALBUMS popular or recent albums in a genre.
     * @param id Genre ID.
     * @param type A string describing the albums to be fetched ('popular' or 'recent').
     * @return a 200 response with a list of albums in the body, otherwise a 404 if the provided id is invalid or
     *         a 400 if the album type is invalid.
     */
    @GetMapping(path = "/{id}/{type}")
    public ResponseEntity<?> getAlbums(@PathVariable Integer id, @PathVariable String type) {
        Optional<Genre> optionalGenre = genreRepository.findById(id);

        if (!optionalGenre.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Album> albums;
        if (type.equals("popular")) {
            albums = albumRepository.findPopularAlbumsByGenreId(id, NUM_ALBUMS);
        } else if (type.equals("recent")) {
            albums = albumRepository.findRecentAlbumsByGenreId(id, NUM_ALBUMS);
        } else {  // invalid album type
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new TypedCollection(albums, Album.class), HttpStatus.OK);
    }
}
