package com.cse308.sbuify.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepo;

    @GetMapping(path = "/{artistId}")
    public ResponseEntity<?> getArtistInfo(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(artist.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/{artistId}/bio")
    public ResponseEntity<?> getArtistBio(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(artist.get().getBio(), HttpStatus.OK);
    }
}
