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
     * Activate or deactivate a song.
     *
     * @param songId
     * @return HTTP.OK when song is successfully updated with partial song, HTTP.NOT_FOUND - id not found
     */
    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSong(@PathVariable Integer songId, @RequestBody Song partialSong) {
        Optional<Song> foundSong = songRepository.findById(songId);
        if (!foundSong.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Song song = foundSong.get();

        if (partialSong.getAlbum() != null){
            song.setAlbum(partialSong.getAlbum());
        }
        if (partialSong.getFeaturedArtists() != null){
            song.setFeaturedArtists(partialSong.getFeaturedArtists());
        }
        if (partialSong.getGenres() != null){
            song.setGenres(partialSong.getGenres());
        }
        if (partialSong.getLength() != null){
            song.setLength(partialSong.getLength());
        }
        if (partialSong.getLyrics() != null){
            song.setLyrics(partialSong.getLyrics());
        }
        if (partialSong.getMBID() != null){
            song.setMbid(partialSong.getMbid());
        }
        if (partialSong.getPlayCount() != null){
            song.setPlayCount(partialSong.getPlayCount());
        }
        if (partialSong.getTrackNumber() != null){
            song.setTrackNumber(partialSong.getTrackNumber());
        }
        songRepository.save(song);
        return new ResponseEntity<>(song,HttpStatus.OK);
    }
}
