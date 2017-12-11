package com.cse308.sbuify.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Controller
public class ImageController {

    private ImageRepository imageRepository;

    private StorageService storageService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Image DEFAULT_IMAGE;

    @Autowired
    public ImageController(
        ImageProperties properties,
        ImageRepository imageRepository,
        StorageService storageService
    ) {
        Optional<Image> optionalImage = imageRepository.findById(properties.getDefaultImageId());

        if (!optionalImage.isPresent()) {
            DEFAULT_IMAGE = null;
            logger.error("Default image is not available.");
        } else {
            DEFAULT_IMAGE = optionalImage.get();
        }

        this.imageRepository = imageRepository;
        this.storageService = storageService;
    }

    /**
     * Get an image in a particular size.
     *
     * @param id ID of image.
     * @param size Image size (default: 'FULL')
     */
    @GetMapping(path = {"/static/i/{id}", "/static/i/{id}/{size}"})
    public ResponseEntity<Resource> serveImage(@PathVariable Integer id, @PathVariable Optional<String> size) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (!optionalImage.isPresent()) {  // invalid image ID
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Image image = optionalImage.get();

        String sizeName;
        if (!size.isPresent()) {
            sizeName = "FULL";
        } else {
            sizeName = size.get().toUpperCase();
        }

        ImageSize imgSize;
        try {
            imgSize = ImageSize.valueOf(sizeName);
        } catch (IllegalArgumentException e) {  // invalid image size
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String path = image.getPath(imgSize);
        if (path == null && DEFAULT_IMAGE != null) { // image doesn't exist in requested size -- use default image
            path = DEFAULT_IMAGE.getPath(imgSize);
        }

        Resource file = storageService.loadAsResource(path);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(14, TimeUnit.DAYS))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
