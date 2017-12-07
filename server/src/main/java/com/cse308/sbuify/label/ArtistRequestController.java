package com.cse308.sbuify.label;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.RequestApprovedEmail;
import com.cse308.sbuify.email.RequestDeniedEmail;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

import static com.cse308.sbuify.label.LabelOwnerController.isOwnerOrAdmin;

@Controller
@RequestMapping(path = "/api/artist-requests")
public class ArtistRequestController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private ArtistRequestRepository artistRequestRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @PostMapping(path = "/{id}/decline")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> declineArtistRequest(@PathVariable Integer requestId) {
        ArtistRequest requestArtist = getArtistRequestById(requestId);
        if (requestArtist == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Email declineEmail = new RequestDeniedEmail(requestArtist);

        if (!(declineEmail.dispatch())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        artistRequestRepository.delete(requestArtist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveArtistRequest(@PathVariable Integer requestId) {
        ArtistRequest requestArtist = getArtistRequestById(requestId);
        if (requestArtist == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Email acceptEmail = new RequestApprovedEmail(requestArtist);

        if (!(acceptEmail.dispatch())) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        artistRequestRepository.delete(requestArtist);
        LabelOwner labelOwner = requestArtist.getLabel();
        Artist artist = requestArtist.getArtist();
        artist.setOwner(labelOwner);
        artistRepository.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> createRequest(@RequestBody ArtistRequest artistRequest) {
        User user = authFacade.getCurrentUser();
        if (artistRequest == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!isOwnerOrAdmin(user, artistRequest.getLabel())) {
           return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        artistRequestRepository.save(artistRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ArtistRequest getArtistRequestById(Integer requestId) {
        Optional<ArtistRequest> requestDb = artistRequestRepository.findById(requestId);
        if (!requestDb.isPresent()) {
            return null;
        }
        return requestDb.get();
    }
}
