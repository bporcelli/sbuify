package com.cse308.sbuify.song;


import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

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
}
