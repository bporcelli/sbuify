package com.cse308.sbuify.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.user.Admin;
import com.cse308.sbuify.user.AdminRepository;

@Controller
@RequestMapping(path = "/admins")
public class AdminController {
	
	@Autowired
	private AdminRepository adminRepo;

	// TODO: This is for proof of concept, need to be removed before deployment
	@GetMapping(path = "/{id}")
	public @ResponseBody Admin findById(@PathVariable int id) {
		Optional<Admin> admin = adminRepo.findById(id);
		
		if(!admin.isPresent())
			return null;
		return admin.get();
	}
	
	@GetMapping
	public @ResponseBody Iterable<Admin> getAllAdmins() {
		// This returns a JSON or XML with the Admins
		return adminRepo.findAll();
	}

	@PostMapping
	public @ResponseBody String addNewAdmin(@RequestBody Admin newAdmin) {
		System.out.println("Asked for new Admin:" + newAdmin);

		if (newAdmin == null)
			return "Failed";

		adminRepo.save(newAdmin);

		return "Success Admin";
	}
	
	@DeleteMapping(path = "/{id}")
	public @ResponseBody Admin deleteById(@PathVariable int id) {
		Admin tmp = new Admin("test.admin@email.com", "123", "Test", "Admin", false);
		return tmp;
	}

	@PatchMapping(path = "/{id}")
	public @ResponseBody Admin patchById(@PathVariable int id) {
//		Admin tmp = new Admin();
		return null;
	}
}
