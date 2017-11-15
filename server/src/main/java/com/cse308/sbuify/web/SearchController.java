package com.cse308.sbuify.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongRepository;

@Controller
@RequestMapping(path = "/api/search")
public class SearchController {

	@Autowired
	private SongRepository songRepo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping(path = "/songs")
	public @ResponseBody String searchSongs(@RequestParam String keyword) {
		logger.debug("Queried keyword: " + keyword);
		Iterable<Song> searchResult = songRepo.findByKeyword(keyword);

		String toRet = "This is what you get: ";
		for (Song s : searchResult) {
			toRet += s;
			System.out.println(s);
		}

		return toRet;
	}
}
