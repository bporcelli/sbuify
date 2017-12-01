package com.cse308.sbuify.stream;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.preferences.Preferences;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.song.SongQuality;
import com.cse308.sbuify.song.SongRepository;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_PREFIX;
import static com.cse308.sbuify.security.SecurityConstants.SECRET;

@Controller
public class StreamController {

	@Autowired
	private StreamRepository streamRepo;

	@Autowired
    private SongRepository songRepo;

	@Autowired
    private UserRepository userRepo;

    @Autowired
	private AuthFacade authFacade;

	/**
	 * Record a stream by the authenticated customer.
	 * @param stream Stream.
	 * @return a 201 response with the recorded stream in the body on success.
	 */
	@PostMapping(path = "/api/customer/streams")
	public ResponseEntity<?> recordStream(@RequestBody Stream stream) {
		Customer customer = (Customer) authFacade.getCurrentUser();
		stream.setCustomer(customer);
		streamRepo.save(stream);
		return new ResponseEntity<>(stream, HttpStatus.CREATED);
	}

	/**
	 * Stream a song.
	 * @param songId ID of the song to stream.
	 * @return a 200 response with the song file in the payload on success, otherwise a 404 if the song
	 * 		   file doesn't exist.
	 */
	@GetMapping(path = "/api/stream/{songId}")
	public ResponseEntity<Resource> streamSong(@PathVariable int songId, @RequestParam String token) {
        User user = getUserFromToken(token);

        if (user == null) {  // invalid auth token
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Customer cust = (Customer) user;

        Optional<Song> optionalSong = songRepo.findById(songId);

        if (!optionalSong.isPresent()) {  // invalid song id
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Song song = optionalSong.get();

		// select song quality
		SongQuality quality = SongQuality.WORST;

        boolean premium = cust.isPremium();
        boolean hq = cust.getPreference(Preferences.HQ_STREAMING, Boolean.class);

        if (premium && hq) {
            quality = SongQuality.BEST;
		}

		// get file as resource
		String path = song.getFilePath(quality);
        if (path == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Resource resource = getFileAsResource(path);
        if (path == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // return file bytes
        return ResponseEntity.ok().body(resource);  // todo: headers?
	}

    private User getUserFromToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace(HEADER_PREFIX, ""))
                .getBody();
        String email = body.getSubject();

        if (email == null) {  // invalid auth token
            return null;
        }

        Optional<User> optionalUser = userRepo.findByEmail(email);

        if (!optionalUser.isPresent()) {
            return null;
        }
        return optionalUser.get();
    }

    private Resource getFileAsResource(String path) {
        Path pathObj = Paths.get(path);
        try {
            Resource resource = new UrlResource(pathObj.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            return null;
        } catch (MalformedURLException e) {
            // todo: log
            return null;
        }
    }
}
