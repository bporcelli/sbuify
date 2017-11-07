package com.cse308.sbuify.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.Preferences;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

@Controller
public class StreamController {

	@Autowired
	private StreamRepository streamRepo;

	private AuthFacade authFacade;

	@PostMapping(path = "/api/customer/streams")
	public ResponseEntity<?> recordStream(@RequestBody Stream stream) {

		User user = authFacade.getCurrentUser();

		if (!(user instanceof Customer)) {
			return new ResponseEntity<>("{}", HttpStatus.FORBIDDEN);
		}

		stream.setCustomer((Customer) user);

		streamRepo.save(stream);

		return new ResponseEntity<>("{}", HttpStatus.OK);
	}
	
	@GetMapping(path = "/api/stream/{songId}")
	public ResponseEntity<?> streamSong(HttpServletResponse response, @RequestParam int songId) {
		Customer cust = (Customer) authFacade.getCurrentUser();

		if (cust == null) {
			return new ResponseEntity<>("{}", HttpStatus.FORBIDDEN);
		}

		boolean premium = cust.isPreminum();

		// select file path
		String path;
		if (premium == true) {
			boolean hq = cust.getPreference(Preferences.HQ_STREAMING, Boolean.class);
			path = getFilePath(songId, hq);
		} else { // if not premium, send none hq file
			path = getFilePath(songId, false);
		}

		// validate the path
		boolean exists = fileExists(path);
		if (!exists) {
			return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
		}

		File mp3File = new File(path);

		response.setContentType("audio/mp3");
		response.setContentLength((int) mp3File.length());

		try {
			InputStream input = new FileInputStream(mp3File);
			
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();

			return new ResponseEntity<>("{}", HttpStatus.OK);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private boolean fileExists(String path) {
		// TODO Auto-generated method stub
		return false;
	}

	private String getFilePath(int songId, boolean hq) {
		// TODO Auto-generated method stub
		return null;
	}
}
