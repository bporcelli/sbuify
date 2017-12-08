package com.cse308.sbuify.playlist;

import com.cse308.sbuify.customer.SavedPlaylist;
import com.cse308.sbuify.customer.SavedPlaylistRepository;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/playlist-folders")
public class PlaylistFolderController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PlaylistFolderRepository folderRepo;

    @Autowired
    private SavedPlaylistRepository savedPlaylistRepo;

    /**
     * Create a playlist folder.
     * @param folder The folder to create.
     * @return a 201 response containing the saved playlist folder in the body.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody PlaylistFolder folder) {
        folder.setOwner(authFacade.getCurrentUser());
        PlaylistFolder saved = folderRepo.save(folder);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Update a playlist folder.
     * @param id Folder ID.
     * @param updated Updated playlist folder.
     * @return an 200 response containing the updated folder on success, otherwise a 404.
     */
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody PlaylistFolder updated) {
        Optional<PlaylistFolder> optionalFolder = folderRepo.findById(id);

        if (!optionalFolder.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlaylistFolder folder = optionalFolder.get();

        if (!SecurityUtils.userCanEdit(folder)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        folder.setName(updated.getName());
        folder = folderRepo.save(folder);

        return new ResponseEntity<>(folder, HttpStatus.OK);
    }

    /**
     * Delete a playlist folder.
     * @param id Folder ID.
     * @return an empty 200 response on success, otherwise a 404 if the folder ID is invalid or a 403
     *         if the current user can't edit the folder.
     */
    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Optional<PlaylistFolder> optionalFolder = folderRepo.findById(id);

        if (!optionalFolder.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlaylistFolder folder = optionalFolder.get();

        if (!SecurityUtils.userCanEdit(folder)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        savedPlaylistRepo.deleteAllByParent(folder);  // todo: ensure that playlist images are deleted
        folderRepo.delete(folder);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get the playlists in a folder.
     * @param id Folder ID.
     * @return a 200 response containing a list of playlists on success, otherwise a 404 if the folder ID
     *         is invalid or a 403 if the folder is not owned by the current user.
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> read(@PathVariable Integer id) {
        Optional<PlaylistFolder> optionalFolder = folderRepo.findById(id);

        if (!optionalFolder.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlaylistFolder folder = optionalFolder.get();

        if (!SecurityUtils.userCanEdit(folder)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<SavedPlaylist> playlists = savedPlaylistRepo.findByParent(folder);

        return new ResponseEntity<>(playlists, HttpStatus.OK);
    }
}