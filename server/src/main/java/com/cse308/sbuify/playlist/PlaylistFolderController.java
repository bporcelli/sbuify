package com.cse308.sbuify.playlist;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

@Controller
@RequestMapping(path = "/api/playlist-folders")
public class PlaylistFolderController {
    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PlaylistFolderRepository fRepo;
    
    @Autowired
    private PlaylistRepository plRepo;

    @PostMapping
    public ResponseEntity<?> createPlaylistFolder(@RequestBody PlaylistFolder pf) {
        User user = authFacade.getCurrentUser();

        pf.setOwner(user);

        PlaylistFolder saved = fRepo.save(pf);

        return new ResponseEntity<PlaylistFolder>(saved, HttpStatus.CREATED);
    }

    @PatchMapping
    // No sequence diagram
    public ResponseEntity<?> updatePlaylistFolder(@PathVariable Integer plfId, @RequestBody String parent_folder) {

        Optional<PlaylistFolder> optPlf = fRepo.findById(Integer.parseInt(parent_folder));
        Optional<PlaylistFolder> optPlfParent = fRepo.findById(Integer.parseInt(parent_folder));

        if (!optPlf.isPresent() || !optPlfParent.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        PlaylistFolder plf = optPlfParent.get();
        plf.setParentFolder(optPlfParent.get());

        fRepo.save(plf);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    

}
