package com.cse308.sbuify.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cse308.sbuify.repository.AlbumRepository;

@Controller
@RequestMapping(path = "/albums")
public class AlbumController {
	@Autowired
	private AlbumRepository albumRepo;
	
	// private AlbumSearchServer searchService;
}
