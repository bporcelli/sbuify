package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/customer/play-queue")
public class PlayQueueController {

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
    public @ResponseBody void updatePlayQueue(@RequestBody PlayQueue newPlayQueue) {
        Customer user = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = user.getPlayQueue();
        pq.update(newPlayQueue.getSongs());
        pqRepo.save(pq);
    }

    /**
     * Add a song/album to the front or back of the customer's play queue
     * @param toAdd Song or album to add.
     * @param first True if song should be added to the front of the queue, otherwise false.
     * @return HTTP response.
     */
    @PostMapping(path = "/add")
    public @ResponseBody void addToPlayQueue(@RequestBody Queueable toAdd,
                                             @RequestParam(required = false, defaultValue = "false") Boolean first) {
        Customer cust = (Customer) authFacade.getCurrentUser();
        PlayQueue pq = cust.getPlayQueue();

        if (first) {
            pq.addAllToFront(toAdd.getItems());
        } else {
            pq.addAll(toAdd.getItems());
        }
        pqRepo.save(pq);
    }

    /**
     * Remove a song/album from the customer's play queue.
     * @param toAdd The song or album to remove.
     * @return HTTP response.
     */
    @PostMapping(path = "/remove")
    public @ResponseBody void removeFromPlayQueue(@RequestBody Queueable toAdd) {
        Customer cust = (Customer) authFacade.getCurrentUser();

        PlayQueue pq = cust.getPlayQueue();
        pq.removeAll(toAdd.getItems());
        pqRepo.save(pq);
    }

    /**
     * Get the customer's play queue.
     * @return HTTP response.
     */
    @GetMapping
    public @ResponseBody PlayQueue getPlayQueue(){
        Customer customer = (Customer) authFacade.getCurrentUser();
        return customer.getPlayQueue();
    }
}
