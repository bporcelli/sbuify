package com.cse308.sbuify.customer;

import java.util.Collection;
import java.util.List;

import com.cse308.sbuify.song.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.security.AuthFacade;
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
    public ResponseEntity<?> updatePlayQueue(@RequestBody PlayQueue newPlayQueue) {
        User user = authFacade.getCurrentUser();

        PlayQueue pq = ((Customer) user).getPlayQueue();
        pq.update(newPlayQueue.getSongs());

        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addToPlayQueue(@RequestBody Queueable qAble) {
        // todo: update to handle optional "first" argument (used by previous song)
        Customer cust = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = cust.getPlayQueue();

        pq.addAll(qAble.getItems());

        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/remove")
    public ResponseEntity<?> removeFromPlayQueue(@RequestBody Queueable qAble) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = cust.getPlayQueue();

        pq.removeAll(qAble.getItems());

        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getPlayQueue(){

        Customer customer = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = customer.getPlayQueue();

        return new ResponseEntity<>(pq, HttpStatus.OK);

    }

}
