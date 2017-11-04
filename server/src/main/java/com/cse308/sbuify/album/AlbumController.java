package com.cse308.sbuify.album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/albums")
public class AlbumController {
	@Autowired
	private AlbumRepository albumRepo;
	
	// private AlbumSearchServer searchService;
}
