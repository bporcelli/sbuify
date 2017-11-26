package com.cse308.sbuify.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class ImageController {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private StorageService storageService;

    /**
     * Get an image in a particular size.
     *
     * @param id ID of image.
     * @param size Image size (default: 'FULL')
     */
    @GetMapping(path = "/static/i/{id}/{size}")
    public ResponseEntity<Resource> serveImage(@PathVariable Integer id, @PathVariable(required = false) String size) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (!optionalImage.isPresent()) {  // invalid image ID
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Image image = optionalImage.get();

        if (size == null) {
            size = "FULL";
        } else {
            size = size.toUpperCase();
        }

        ImageSize imgSize;
        try {
            imgSize = ImageSize.valueOf(size);
        } catch (IllegalArgumentException e) {  // invalid image size
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String path = image.getPath(imgSize);
        if (path == null) { // image doesn't exist in requested size
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Resource file = storageService.loadAsResource(path);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
