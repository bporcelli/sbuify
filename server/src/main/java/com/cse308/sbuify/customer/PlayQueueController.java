package com.cse308.sbuify.customer;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

@Controller
@RequestMapping(path = "/api/customer/play-queue")
public class PlayQueueController {

    @Autowired
    private PlayQueueRepository pqRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthFacade authFacade;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePlayQueue(@RequestBody List<Song> songs) {
        User user = authFacade.getCurrentUser();

        PlayQueue pq = ((Customer) user).getPlayQueue();
        pq.update(songs);

        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addToPlayQueue(@RequestBody List<Song> songs) {
        // todo: update to handle optional "first" argument (used by previous song)
        User user = authFacade.getCurrentUser();

        if (!(user instanceof Customer) || user == null) {
            return new ResponseEntity<>("{}", HttpStatus.FORBIDDEN);
        }

        Customer cust = (Customer) user;

        PlayQueue pq = cust.getPlayQueue();

        pq.addAll(songs);

        pqRepo.save(pq);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PostMapping(path = "/remove")
    public ResponseEntity<?> removeFromPlayQueue(@RequestBody List<Song> songs) {
        User user = authFacade.getCurrentUser();

        if (!(user instanceof Customer) || user == null) {
            return new ResponseEntity<>("{}", HttpStatus.FORBIDDEN);
        }

        Customer cust = (Customer) user;

        PlayQueue pq = cust.getPlayQueue();

        pq.removeAll(songs);

        pqRepo.save(pq);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
