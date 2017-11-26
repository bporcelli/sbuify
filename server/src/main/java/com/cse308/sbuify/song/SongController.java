package com.cse308.sbuify.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    /**
     * Activate or deactivate a song.
     *
     * @param songId
     * @return HTTP.OK when song is successfully activated/deactivated, HTTP.NOT_FOUND - id not found
     */
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> activateDeactivateSong(@PathVariable("id") String songId) {
        // TODO: Use a different endpoint or rewrite to handle generic updates to songs (not just activation/deactivation)
        Optional<Song> foundSong = songRepository.findById(Integer.valueOf(songId));

        if (!foundSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = foundSong.get();

        boolean newActiveStatus = !song.isActive();
        song.setActive(newActiveStatus);
        songRepository.save(song);

        return new ResponseEntity<>(song,HttpStatus.OK);
    }
}
