package com.cse308.sbuify.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
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

    @PutMapping
    public ResponseEntity<?> play(@RequestBody List<Song> songs) {
    	User user = authFacade.getCurrentUser();
    	
    	PlayQueue pq = ((Customer) user).getPlayQueue();
    	pq.update(songs);
    	
    	pqRepo.save(pq);

        return new ResponseEntity<>("{}", HttpStatus.ACCEPTED);
    }

}
