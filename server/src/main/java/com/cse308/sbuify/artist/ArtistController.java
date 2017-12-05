package com.cse308.sbuify.artist;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.image.Base64Image;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.label.Label;
import com.cse308.sbuify.label.LabelOwner;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ArtistController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping(path = "/api/{artistId}")
    public ResponseEntity<?> getArtistInfo(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(artist.get(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/{artistId}/bio")
    public ResponseEntity<?> getArtistBio(@PathVariable Integer artistId) {
        Optional<Artist> artist = artistRepo.findById(artistId);
        if (!artist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(artist.get().getBio(), HttpStatus.OK);
    }

    @GetMapping(path = "/api/artists/{artistId}/related")
    public ResponseEntity<?> getRelatedArtist(@PathVariable Integer artistId) {
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if (!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        Set<Artist> relatedArtist = new HashSet<>();
        List<Album> artistAlbum = artist.getAlbums();

        for (Album album :artistAlbum){
            Set<Song> songs = album.getSongs();
            for (Song song: songs){
                Set<Artist> artists = song.getFeaturedArtists();
                for (Artist featureArtist: artists){
                    if(!featureArtist.equals(artist)){
                        relatedArtist.add(featureArtist);
                    }
                }
            }
        }

        TypedCollection featuredArtist = new TypedCollection(relatedArtist, Artist.class);

        return new ResponseEntity<>(featuredArtist, HttpStatus.OK);
    }

    @PatchMapping(path = "/api/label/artists/{artistId}")
    @PreAuthorize("hasRole('ROLE_LABEL')")
    public ResponseEntity<?> updateArtist(@PathVariable Integer artistId, @RequestBody Artist partialArtist){
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if(!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        System.out.println("ARRIVES");
        if(!underLabelorAdmin(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if(partialArtist.getAlbums() != null){
            artist.setAlbums(partialArtist.getAlbums());
        }
        if(partialArtist.getAliases() != null){
            artist.setAliases(partialArtist.getAliases());
        }
        if(partialArtist.getCoverImage() != null){
            Image image = null;
            Base64Image rawImage = (Base64Image) partialArtist.getImage();
            try {
                image = storageService.save(rawImage.getDataURL());
            } catch (StorageException ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
            artist.setImage(image);
        }
        if(partialArtist.getMerchandise() != null){
            artist.setMerchandise(partialArtist.getMerchandise());
        }
        if(partialArtist.getMonthlyListeners() != null){
            artist.setMonthlyListeners(partialArtist.getMonthlyListeners());
        }
        if(partialArtist.getRelatedArtists() != null){
            artist.setRelatedArtists(partialArtist.getRelatedArtists());
        }

        artistRepo.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);


    }


    @PostMapping(path = "/api/label/artists/{artistId}/merchandise/")
    @PreAuthorize("hasAnyRole('ROLE_LABEL','ADMIN')")
    public ResponseEntity<?> addMerchandise(@PathVariable Integer artistId, @RequestBody Product merch){
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if(!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if(!underLabelorAdmin(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Set<Product> artistMerch = artist.getMerchandise();
        artistMerch.add(merch);
        artistRepo.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);

    }


    @PatchMapping(path = "/api/label/artists/{artistId]/merchandise/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_LABEL','ADMIN')")
    public ResponseEntity<?> updateMerchandise(@PathVariable(value = "artistId") Integer artistId,
                                               @PathVariable(value = "idtemId")  Integer itemId,
                                               @RequestBody Product productUpdate){

        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if(!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if(!underLabelorAdmin(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Product> optionalProduct = productRepository.findById(itemId);
        if(!optionalProduct.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productRepository.save(productUpdate);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(path = "/api/label/artists/{artistId]/merchandise/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_LABEL','ADMIN')")
    public ResponseEntity<?> deleteMerchandise(@PathVariable(value = "artistId") Integer artistId,
                                               @PathVariable(value = "idtemId")  Integer itemId){
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if(!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if(!underLabelorAdmin(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Product> optionalProduct = productRepository.findById(itemId);
        if(!optionalProduct.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Product product = optionalProduct.get();

        Set<Product> artistMerch = artist.getMerchandise();

        artistMerch.remove(product);

        artistRepo.save(artist);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping(path = "/api/label/artists/{artistId}")
    @PreAuthorize("hasAnyRole('ROLE_LABEL','ADMIN')")
    public ResponseEntity<?> deleteArtist(@PathVariable(value = "artistId") Integer artistId){
        Optional<Artist> optionalArtist = artistRepo.findById(artistId);
        if(!optionalArtist.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Artist artist = optionalArtist.get();
        if(!underLabelorAdmin(artist)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        LabelOwner label = (LabelOwner)authFacade.getCurrentUser();
        Set<Artist> labelArtists = label.getArtists();
        labelArtists.remove(artist);
        userRepository.save(label);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean underLabelorAdmin(Artist artist){
        LabelOwner labelOwner = (LabelOwner)authFacade.getCurrentUser();
        Set<Artist> artists = labelOwner.getArtists();
        User user = authFacade.getCurrentUser();
        return artists.contains(artist) || user instanceof Admin;
    }

}
