package com.cse308.sbuify.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    /**
     * Update a song.
     *
     * @param id Song ID.
     * @return a 200 response if the song is updated, otherwise a 404 if the song ID is invalid.
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSong(@PathVariable Integer id, @RequestBody Song updated) {
        Optional<Song> foundSong = songRepository.findById(id);

        if (!foundSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Song song = foundSong.get();

        // active state, lyrics, and genres are editable
        if (updated.isActive() != null) {
            song.setActive(updated.isActive());
        }
        if (updated.getGenres() != null){
            song.setGenres(updated.getGenres());
        }
        if (updated.getLyrics() != null){
            song.setLyrics(updated.getLyrics());
        }

        songRepository.save(song);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }
}
