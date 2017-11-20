package com.cse308.sbuify.test.helper;

import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;

import static com.cse308.sbuify.security.SecurityConstants.*;

public class AuthUtils {

    @Autowired
    protected UserRepository userRepository;

	/**
	 * Helper: send a login request and return the response.
	 */
	public static ResponseEntity<Void> sendLoginRequest(int port, TestRestTemplate restTemplate, User user) {
		return restTemplate.postForEntity("http://localhost:" + port + "/api/login", user, Void.class);
	}

    /**
     * Helper: generate a random email address.
     */
    public static String generateEmail() {
        Integer curTime = Instant.now().getNano();
        return Integer.toString(curTime) + "@test.com";
    }

    /**
     * Helper: check a response for a valid JWT.
     */
    public static void checkToken(ResponseEntity<?> response, User user) {
        String JWT = response.getHeaders().getFirst(HEADER_NAME);

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(JWT.replace(HEADER_PREFIX, ""))
                .getBody();

        String email = claims.getSubject();
        ArrayList<String> scopes = (ArrayList<String>) claims.get("scopes");

        boolean emailMatches = user.getEmail().equals(email);
        boolean roleMatches = scopes.equals(SecurityUtils.getAuthorityStrings(user));

        assert(emailMatches && roleMatches);
    }
}
