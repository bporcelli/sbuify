package com.cse308.sbuify.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.domain.Preference;
import com.cse308.sbuify.repository.PreferenceRepository;

@Controller
@RequestMapping(path = "/customers/{id}/preferences")

//TODO
public class PreferenceController {
	@Autowired
	private PreferenceRepository preferenceRepo;
	
	@GetMapping
	public @ResponseBody Preference findById(@PathVariable int id) {
		Preference pref = new Preference();
		return pref;
	}
	
	@PatchMapping
	public @ResponseBody Preference updateById(@PathVariable int id) {
		//TODO
		return null;
	}
}
