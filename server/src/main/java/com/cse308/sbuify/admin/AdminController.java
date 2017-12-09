package com.cse308.sbuify.admin;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/admins/")
public class AdminController {

    @Autowired
    private AuthFacade authFacade;
	
	@Autowired
	private AdminRepository adminRepo;

	@Autowired
    private UserRepository userRepository;

	// TODO: This is for proof of concept, need to be removed before deployment

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
     * @return Http.CREATED, successful, Http.FORBIDDEN, no permission, HTTP.BAD_REQUEST, invalid request body
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
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, invalid id
     */
	@PatchMapping(path = "{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody Admin partialAdmin) {
	    Admin admin = getAdminById(id);

	    if (admin == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!isSuperAdmin()) {
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

        userRepository.save(admin);
        return new ResponseEntity<>(HttpStatus.OK);
	}

    public Admin getAdminById(Integer id){
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
