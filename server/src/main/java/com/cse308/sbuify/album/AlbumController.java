package com.cse308.sbuify.album;

import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/albums")
public class AlbumController {

	@Autowired
	private AlbumRepository albumRepo;

    public final Integer NUM_NEW_RELEASES;

    @Autowired
    public AlbumController(AlbumProperties albumProperties){
        NUM_NEW_RELEASES = albumProperties.getNumNewReleases();
    }

    /**
     * Get new releases.
     * @param page Page index.
     * @return a 200 response containing a list of at most NUM_NEW_RELEASES albums on success.
     */
    @GetMapping(path = "/new-releases")
    @PreAuthorize("hasRole('CUSTOMER')")
    public @ResponseBody TypedCollection getNewReleases(@RequestParam Integer page) {
        Page<Album> recent = albumRepo.findRecent(PageRequest.of(page, NUM_NEW_RELEASES));
        ArrayList<Album> albums = new ArrayList<>();
        for (Album album: recent) {
            albums.add(album);
        }
        return new TypedCollection(albums, Album.class);
    }

    /**
     * Get information about an album.
     * @param id Album ID.
     * @return a 200 response containing the album with the given ID, otherwise a 404 if the album ID
     *         is invalid.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @DecorateResponse(type = Album.class)
    public @ResponseBody ResponseEntity<?> getAlbum(@PathVariable Integer id) {
        Optional<Album> optionalAlbum = albumRepo.findById(id);
        if (!optionalAlbum.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalAlbum.get(), HttpStatus.OK);
    }
}
