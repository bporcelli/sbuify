package com.cse308.sbuify.artist;

import java.util.Optional;

import javax.activity.InvalidActivityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/artists")
public class ArtistController {
    @Autowired
    private ArtistRepository artistRepo;

    @GetMapping(path = "/{artistId}")
    public ResponseEntity<?> getArtistInfo(@PathVariable Integer artistId) throws InvalidActivityException {
        Optional<Artist> artist = artistRepo.findById(artistId);

        if (!artist.isPresent())
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Artist>(artist.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/{artistId}/bio")
    public ResponseEntity<?> getArtistBio(@PathVariable Integer artistId) throws InvalidActivityException {
        Optional<Artist> artist = artistRepo.findById(artistId);

        if (!artist.isPresent())
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<Biography>(artist.get().getBio(), HttpStatus.OK);
    }
}
