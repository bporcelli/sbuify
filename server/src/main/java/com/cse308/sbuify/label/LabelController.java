package com.cse308.sbuify.label;

import com.cse308.sbuify.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;

    @PatchMapping(path = "/api/record-labels/{labelId}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> updateRecordLabel(@PathVariable Integer labelId, @RequestBody Label partialLabel) {
        Label label = getRecordLabelById(labelId);

        if (label == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(label)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (partialLabel.getOwner() != null) {
            label.setOwner(partialLabel.getOwner());
        }
        if (partialLabel.getName() != null) {
            label.setName(partialLabel.getName());
        }
        if (partialLabel.getAddress() != null) {
            label.setAddress(partialLabel.getAddress());
        }

        labelRepository.save(label);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Label getRecordLabelById(Integer labelId) {
        Optional<Label> label = labelRepository.findById(labelId);
        if (!label.isPresent()) {
            return null;
        }
        return label.get();
    }
}
