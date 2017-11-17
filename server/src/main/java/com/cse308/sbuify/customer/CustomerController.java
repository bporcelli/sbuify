package com.cse308.sbuify.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

@Controller
@RequestMapping(path = "/api/customer/")
public class CustomerController {

    @Autowired
    private AuthFacade authFacade;

    @PostMapping(path = "/songs")
    public ResponseEntity<?> saveSongToLibrary(@RequestBody Song song) {
        User user = authFacade.getCurrentUser();

        if (!(user instanceof Customer))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Customer cust = (Customer) user;

        cust.getLibrary().add(song);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
