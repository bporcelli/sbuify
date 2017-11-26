package com.cse308.sbuify.song;


import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/songs")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private AuthFacade authFacade;

    /**
     *  Get all song from db
     * @return TypeCollection wrapper for the list of all songs
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody TypedCollection getAllSongs(){

        Iterable<Song> dbSongs = songRepository.findAll();

        List<Song> songs = new ArrayList<>();
        for (Song song: dbSongs) {
            songs.add(song);
        }

        return new TypedCollection(songs, Song.class);

    }

    /**
     *
     * @param songId
     * @return HTTP.OK when song is successfully activated/deactivated, HTTP.NOT_FOUND - id not found
     */
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> activateDeactivateSong(@PathVariable("id") String songId){

        Optional<Song> foundSong = songRepository.findById(Integer.valueOf(songId));

        if ( !foundSong.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = foundSong.get();

        boolean newActiveStatus = !song.isActive();

        song.setActive(newActiveStatus);

        songRepository.save(song);

        return new ResponseEntity<>(song,HttpStatus.OK);

    }
}
