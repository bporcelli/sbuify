package com.cse308.sbuify.playlist;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;

@Controller
@RequestMapping(path = "/api/customer/songs")
public class LibraryController {
    @Autowired
    private LibraryRepository libraryRepo;

    @Autowired
    private SongRepository songRepo;

    @Autowired
    private AuthFacade authFacade;

    @GetMapping
    public @ResponseBody ResponseEntity<Library> getSongs() {
        Customer cust = (Customer) authFacade.getCurrentUser();

        Library lib = cust.getLibrary();

        return new ResponseEntity<>(lib, HttpStatus.OK);
    }

    @PostMapping
    public @ResponseBody ResponseEntity<SavedSong> saveSongToLibrary(@RequestBody Song songToAdd) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        Library lib = cust.getLibrary();

        SavedSong ss = lib.add(songToAdd);

        libraryRepo.save(lib);

        return new ResponseEntity<>(ss, HttpStatus.OK);
    }

    @DeleteMapping
    public @ResponseBody ResponseEntity<?> saveSongToLibrary(@RequestParam Integer songToRmId) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        Library lib = cust.getLibrary();

        Optional<Song> songToRm = songRepo.findById(songToRmId);

        if (!songToRm.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        lib.remove(songToRm.get()); // Returns SavedSong but not declaring it because we are not using it

        libraryRepo.save(lib);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
