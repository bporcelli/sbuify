package com.cse308.sbuify.artist;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.image.Base64Image;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.song.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(path = "/api/artists")
public class ArtistController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private StorageService storageService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AlbumRepository albumRepository;

    /**
     * Get basic information about an artist.
     * @param artistId ID of artist.
     */
    @GetMapping(path = "/{artistId}")
    @DecorateResponse(type = Artist.class)
    public ResponseEntity<?> getArtistInfo(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent()) {
            LOGGER.warn("Artist not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(artist.get(), HttpStatus.OK);
    }

    /**
     * Get an artist's biography.
     * @param artistId Artist ID.
     */
    @GetMapping(path = "/{artistId}/bio")
    public ResponseEntity<?> getArtistBio(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent()) {
            LOGGER.warn("Artist not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(artist.get().getBio(), HttpStatus.OK);
    }

    /**
     * Get other artists related to an artist.
     * @param id ID of artist.
     * @param number Optional number of related artists to get (default: 5).
     * @param offset Offset to first artist.
     */
    @GetMapping(path = {"/{id}/related", "/{id}/related/{number}"})
    @DecorateResponse(type = TypedCollection.class)
    public ResponseEntity<?> getRelatedArtists(@PathVariable Integer id,
                                               @PathVariable Optional<Integer> number,
                                               @RequestParam Integer offset) {
        Optional<Artist> optionalArtist = artistRepo.findById(id);

        if (!optionalArtist.isPresent()) {
            LOGGER.warn("Artist not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Integer numArtists = 5;

        if (number.isPresent()) {
            numArtists = number.get();
        }

        Page<Artist> related = artistRepo.getRelatedByArtistId(id, PageRequest.of(offset, numArtists));
        List<Artist> artistList = new ArrayList<>();
        for (Artist artist: related) {
            artistList.add(artist);
        }
        TypedCollection relatedArtists = new TypedCollection(artistList, Artist.class);
        return new ResponseEntity<>(relatedArtists, HttpStatus.OK);
    }

    /**
     * Update an artist.
     * @param artistId ID of artist to update.
     * @param updated Updated artist object with fields that shouldn't be updated set to null.
     * @return an empty 200 response on success, otherwise a 404 if the artist is invalid or a 403 if the
     *         artist can't be edited by the current user.
     */
    @PatchMapping(path = "/{artistId}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> updateArtist(@PathVariable Integer artistId, @RequestBody Artist updated) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (updated.getName() != null) {
            artist.setName(updated.getName());
        }
        if (updated.getCoverImage() != null) {
            Image image;
            Base64Image rawImage = (Base64Image) updated.getImage();
            try {
                image = storageService.save(rawImage.getDataURL());
            } catch (StorageException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            if (artist.getCoverImage() != null){
                this.storageService.delete(artist.getCoverImage());
            }
            artist.setImage(image);
        }
        if (updated.getAliases() != null) {
            artist.setAliases(updated.getAliases());
        }

        artistRepo.save(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update an artist's biography.
     * @param artistId ID of artist.
     * @param bio New biography.
     * @return a 200 response on success, otherwise a 404 if the artist ID is invalid or a 403 if the
     *         current user can't edit the artist.
     */
    @PutMapping(path = "/{artistId}/bio")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> updateBiography(@PathVariable Integer artistId, @RequestBody Biography bio) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // todo: all images from the frontend will be base64 encoded; update code accordingly
        if (bio.getImages() != null) {
            List<Image> images = bio.getImages();
            Image updatedImage;
            List<Image> updatedImages = new ArrayList<>();
            for (Image image: images) {
                try{
                    updatedImage = this.storageService.save(image.getPath());
                    updatedImages.add(updatedImage);
                } catch (StorageException ex){
                    LOGGER.error("Image storage error:", ex.getMessage());
                    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }
            images = artist.getBio().getImages();
            images.forEach(image -> this.storageService.delete(image));
        }

        artist.setBio(bio);
        artistRepo.save(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Get the merchandise for an artist.
     * @param id Artist ID.
     */
    @GetMapping(path = "/{id}/merchandise")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    public ResponseEntity<?> getMerchandise(@PathVariable Integer id) {
        Optional<Artist> optionalArtist = artistRepo.findById(id);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Product> products = productRepository.getAllByArtist(optionalArtist.get());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Get an artist's albums.
     * @param id Artist ID.
     */
    @GetMapping(path = "/{id}/albums")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    @DecorateResponse(type = TypedCollection.class)
    public ResponseEntity<?> getAlbums(@PathVariable Integer id) {
        Optional<Artist> optionalArtist = artistRepo.findById(id);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Album> albums = albumRepository.getAlbumsByArtist(optionalArtist.get());
        // convert to TypedCollection so we can decorate the response
        TypedCollection collection = new TypedCollection(albums, Album.class);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    /**
     * Create a new merchandise item for an artist.
     * @param artistId ID of artist.
     * @param product New merchandise item.
     * @return a 201 response containing the saved product on success, otherwise a 404 if the artist ID is
     *         invalid or a 403 if the current user can't edit the artist.
     */
    @PostMapping(path = "/{artistId}/merchandise")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> addMerchandise(@PathVariable Integer artistId, @RequestBody Product product) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        product.setArtist(artist);
        Product saved = productRepository.save(product);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * Update an artist's merchandise.
     * @param artistId Artist ID.
     * @param itemId ID of product to update.
     * @param updated Updated product object, with fields that shouldn't be changed set to null.
     * @return a 200 response on success, otherwise a 404 if the artist ID is invalid, or a 403 if the
     *         current user can't edit the merchandise item.
     */
    @PutMapping(path = "/{artistId]/merchandise/{itemId}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> updateMerchandise(@PathVariable Integer artistId,
                                               @PathVariable Integer itemId,
                                               @RequestBody Product updated) {

        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Product> optionalProduct = productRepository.findById(itemId);
        if (!optionalProduct.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product product = optionalProduct.get();

        if (!artist.removeProduct(product)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        artist.addProduct(updated);
        artistRepo.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a merchandise item.
     * @param artistId Artist ID.
     * @param itemId ID of item to delete.
     * @return an empty 200 response on success, otherwise a 404 if the artist ID is invalid
     *         or a 403 if the current user can't delete the specified item.
     */
    @DeleteMapping(path = "/{artistId]/merchandise/{itemId}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> deleteMerchandise(@PathVariable Integer artistId,
                                               @PathVariable Integer itemId) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Product> optionalProduct = productRepository.findById(itemId);
        if (!optionalProduct.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Product product = optionalProduct.get();

        if (!artist.removeProduct(product)){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        artistRepo.save(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete an artist from label record.
     * @param artistId ID of artist to delete.
     * @return a 200 response on success, otherwise a 404 if the artist ID is invalid or
     *         a 403 if the current user can't delete the specified artist.
     */
    @DeleteMapping(path = "/artists/{artistId}")
    @PreAuthorize("hasAnyRole('LABEL', 'ADMIN')")
    public ResponseEntity<?> deleteArtist(@PathVariable Integer artistId) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if (!SecurityUtils.userCanEdit(artist)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        artist.setOwner(null);
        artistRepo.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
