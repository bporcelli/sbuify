package com.cse308.sbuify.admin;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.album.AlbumRepository;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.artist.ArtistRepository;
import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping(path = "/api/admins/")
public class AdminController {

    @Autowired
    private AuthFacade authFacade;
	
	@Autowired
	private AdminRepository adminRepo;

	@Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    /**
     * Get admin by Id
     * @param id
     * @return Http.OK successful, Http.NOT_FOUND not found
     */
	@GetMapping(path = "{id}")
	public ResponseEntity<?> findById(@PathVariable Integer id) {
		Admin admin = getAdminById(id);
		if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
		return new ResponseEntity<>(admin, HttpStatus.OK);
	}

    /**
     * get all admins
     * @return Http.OK successful
     */
	@GetMapping
	public @ResponseBody TypedCollection getAllAdmins() {
        Iterable<Admin> adminIterable = adminRepo.findAll();
        Set<Admin> admins = new HashSet<>();
        adminIterable.forEach(labelOwner -> admins.add(labelOwner));
        return new TypedCollection(admins, Admin.class);
	}

    /**
     * Create an admin
     * @param admin
     * @return Http.CREATED, successful, Http.FORBIDDEN, no permission,
     *         HTTP.BAD_REQUEST, invalid request body
     */
	@PostMapping
	public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
		if (admin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!isSuperAdmin()){
		    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userRepository.save(admin);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

    /**
     * Delete an admin given Id
     * @param id
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, invalid id
     */
	@DeleteMapping(path = "{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
	    Admin admin = getAdminById(id);
	    if (admin == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!isSuperAdmin()) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userRepository.delete(admin);
        return new ResponseEntity<>(HttpStatus.OK);
	}

    /**
     * Update an admin
     * @param id
     * @param partialAdmin
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND,
     *         invalid id
     */
	@PatchMapping(path = "{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody Admin partialAdmin) {
	    User currentUser = authFacade.getCurrentUser();

	    Admin admin = getAdminById(id);

        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!currentUser.equals(admin) && !isSuperAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (partialAdmin.isSuperAdmin() != null) {
            admin.setSuperAdmin(partialAdmin.isSuperAdmin());
        }
        if (partialAdmin.getFirstName() != null) {
            admin.setFirstName(partialAdmin.getFirstName());
        }
        if (partialAdmin.getLastName() != null) {
            admin.setLastName(partialAdmin.getLastName());
        }

        admin = userRepository.save(admin);

	    return new ResponseEntity<>(admin, HttpStatus.OK);
	}

    @PatchMapping(path = "/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if (!userOpt.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        User user = userOpt.get();
        if (user instanceof Admin && !isSuperAdmin()) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        Customer cust = (Customer) user;
        // todo set inactive

        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping(path = "/artist/")
    public ResponseEntity<?> addArtistManually(@RequestBody Artist artist) {
        Optional<Artist> optArtist = artistRepository.findByName(artist.getName());

        if (optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }

        Artist saved = artistRepository.save(artist);

        return new ResponseEntity<Artist>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/artist/{id}")
    public ResponseEntity<?> rmArtistManually(@PathVariable Integer id) {
        Optional<Artist> optArtist = artistRepository.findById(id);

        if (!optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping(path = "/album/")
    public ResponseEntity<?> addAlbumManually(@RequestBody Album album) {
        Album saved = albumRepository.save(album);

        return new ResponseEntity<Album>(saved, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/album/{id}")
    public ResponseEntity<?> rmAlbumManually(@PathVariable Integer id) {
        Optional<Album> optArtist = albumRepository.findById(id);

        if (!optArtist.isPresent()) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public Admin getAdminById(Integer id) {
        Optional<Admin> admin = adminRepo.findById(id);
        if (!admin.isPresent()) {
            return null;
        }
        return admin.get();
    }

	private boolean isSuperAdmin(){
	    User user = authFacade.getCurrentUser();
	    if (!(user instanceof Admin)) {
	        return false;
        }
        Admin admin = (Admin) user;
	    return admin.isSuperAdmin();
    }
}
