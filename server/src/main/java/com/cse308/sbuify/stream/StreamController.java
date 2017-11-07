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
import org.springframework.security.access.prepost.PreAuthorize;
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

	@Autowired
	private AuthFacade authFacade;

	@PostMapping(path = "/api/customer/streams")
	public ResponseEntity<?> recordStream(@RequestBody Stream stream) {
		Customer customer = (Customer) authFacade.getCurrentUser();

		stream.setCustomer(customer);

		streamRepo.save(stream);

		// todo: return saved stream object with 201 header
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping(path = "/api/stream/{songId}")
    @PreAuthorize("hasRole('CUSTOMER')") // todo: change api endpoint to /api/customer/stream so we don't need this?
	public ResponseEntity<?> streamSong(HttpServletResponse response, @RequestParam int songId) {
		Customer cust = (Customer) authFacade.getCurrentUser();

		// select file path
        boolean premium = cust.isPreminum();
        boolean hq = false;

		if (premium == true) {
            hq = cust.getPreference(Preferences.HQ_STREAMING, Boolean.class);
		}

        String path = getFilePath(songId, hq);

		// validate the path
		boolean exists = fileExists(path);
		if (!exists) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		File mp3File = new File(path);

		response.setContentType("audio/mp3");
		response.setContentLength((int) mp3File.length());

		try {
			InputStream input = new FileInputStream(mp3File);
			
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();

			// todo: body should be song bytes (not sure about return type used)
			return new ResponseEntity<>("{}", HttpStatus.OK);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
