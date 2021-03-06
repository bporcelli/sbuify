package com.cse308.sbuify.label;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/api/label-owner/")
public class LabelOwnerController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private LabelOwnerRepository labelOwnerRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all Label Owners
     * @return
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody TypedCollection getAllLabelOwners() {
        Iterable<LabelOwner> labelOwnerIterable = labelOwnerRepository.findAll();
        Set<LabelOwner> labelOwners = new HashSet<>();
        labelOwnerIterable.forEach(labelOwner -> labelOwners.add(labelOwner));
        return new TypedCollection(labelOwners, LabelOwner.class);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<?> getLabelOwner(@PathVariable Integer labelOwnerId) {
        LabelOwner labelOwner = getLabelOwnerById(labelOwnerId);
        if (labelOwner == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(labelOwner, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLabelOwner(@RequestBody LabelOwner labelOwner) {
        if (labelOwner == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userRepository.save(labelOwner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> updateLabelOwner(@PathVariable Integer labelOwnerId, @RequestBody LabelOwner updated) {
        LabelOwner labelOwner = getLabelOwnerById(labelOwnerId);

        User user = authFacade.getCurrentUser();
        if (!isOwnerOrAdmin(user, labelOwner)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (updated.getName() != null) {
            labelOwner.setName(updated.getName());
        }
        if (updated.getLabel() != null) {  // todo: user editable?
            labelOwner.setLabel(updated.getLabel());
        }

        userRepository.save(labelOwner);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private LabelOwner getLabelOwnerById(Integer id) {
        Optional<LabelOwner> labelOwnerOptional = labelOwnerRepository.findById(id);
        if (!labelOwnerOptional.isPresent()) {
            return null;
        }
        return labelOwnerOptional.get();
    }

    // todo: move this and similar reused methods to a utility class; having shared utilities in a controller is inappropriate
    protected static boolean isOwnerOrAdmin(User user, LabelOwner labelOwner) {
        boolean owner = false;
        if (user instanceof LabelOwner) {
            LabelOwner userLabelOwner = (LabelOwner) user;
            if (userLabelOwner.getId().equals(labelOwner.getId())) {
                owner = true;
            }
        }
        return owner || (user instanceof Admin);
    }
}
