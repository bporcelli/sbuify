package com.cse308.sbuify.playlist;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/playlist-folders")
public class PlaylistFolderController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PlaylistFolderRepository folderRepo;
    
    @Autowired
    private PlaylistRepository playlistRepo;

    /**
     * Create a playlist folder.
     * @param folder The folder to create.
     * @return a 201 response containing the saved playlist folder in the body.
     */
    @PostMapping
    public ResponseEntity<?> createPlaylistFolder(@RequestBody PlaylistFolder folder) {
        folder.setOwner(authFacade.getCurrentUser());
        PlaylistFolder saved = folderRepo.save(folder);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Update a playlist folder.
     * @param id Folder ID.
     * @param updated Updated playlist folder.
     * @return an empty 200 response if the folder is updated successfully, otherwise a 404.
     */
    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updatePlaylistFolder(@PathVariable Integer id, @RequestBody PlaylistFolder updated) {
        // changed to handle all playlist folder updates instead of just changes to the parent folder
        Optional<PlaylistFolder> optionalFolder = folderRepo.findById(id);

        if (!optionalFolder.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        PlaylistFolder folder = optionalFolder.get();

        folder.setName(updated.getName());
        folder.setParentFolder(updated.getParentFolder());
        folder.setPosition(updated.getPosition());

        folderRepo.save(folder);  // if the parent folder ID is invalid, this should trigger a 500 response
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}