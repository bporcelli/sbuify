package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/customer/play-queue")
public class PlayQueueController {

    // todo: use ResponseBody unless control over HTTP status code is required

    @Autowired
    private PlayQueueRepository pqRepo;

    @Autowired
    private AuthFacade authFacade;

    /**
     * Update the customer's play queue.
     * @param newPlayQueue
     * @return HTTP response.
     */
    @PutMapping
    public ResponseEntity<?> updatePlayQueue(@RequestBody PlayQueue newPlayQueue) {
        Customer user = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = user.getPlayQueue();
        pq.update(newPlayQueue.getSongs());
        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Add a song/album to the front or back of the customer's play queue
     * @param toAdd Song or album to add.
     * @param first True if song should be added to the front of the queue, otherwise false.
     * @return HTTP response.
     */
    @PostMapping(path = "/add")
    public ResponseEntity<?> addToPlayQueue(@RequestBody Queueable toAdd,
                                            @RequestParam(required = false, defaultValue = "false") Boolean first) {
        Customer cust = (Customer) authFacade.getCurrentUser();
        PlayQueue pq = cust.getPlayQueue();

        if (first) {
            pq.addAllToFront(toAdd.getItems());
        } else {
            pq.addAll(toAdd.getItems());
        }

        pqRepo.save(pq);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Remove a song/album from the customer's play queue.
     * @param toAdd The song or album to remove.
     * @return HTTP response.
     */
    @PostMapping(path = "/remove")
    public ResponseEntity<?> removeFromPlayQueue(@RequestBody Queueable toAdd) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = cust.getPlayQueue();
        pq.removeAll(toAdd.getItems());
        pqRepo.save(pq);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get the customer's play queue.
     * @return HTTP response.
     */
    @GetMapping
    public ResponseEntity<?> getPlayQueue(){
        Customer customer = (Customer) authFacade.getCurrentUser();
        return new ResponseEntity<>(customer.getPlayQueue(), HttpStatus.OK);
    }

    /**
     *
     * @param partial Entity type - Play Queue
     * @return HTTP.OK update success, HTTP.NOT_FOUND, play queue not found
     */
    @PatchMapping(path = "play-queue")
    public ResponseEntity<?> patchPlayQueue(@RequestBody PlayQueue partial){
        Customer customer = (Customer)authFacade.getCurrentUser();
        PlayQueue customerPlayQueue = customer.getPlayQueue();

        if(partial.getSongs() != null){
            customerPlayQueue.setSongs(partial.getSongs());
            pqRepo.save(customerPlayQueue);
        }

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
