package com.cse308.sbuify.test;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;

import static com.cse308.sbuify.security.SecurityConstants.*;
import static com.cse308.sbuify.test.TestUtils.randomEmail;
import static org.junit.Assert.assertEquals;

/**
 * Login & registration tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Test: does authentication succeed when a correct password is provided?
	 */
	@Test
	public void authenticationSucceedsWithCorrectPassword() {
        ResponseEntity<Void> response;

		// Create users
		Customer customer = new Customer(randomEmail(), "12345", "Jane", "Doe", new Date());
        registerUser(customer);

        Admin admin = new Admin(randomEmail(), "123", "Test", "Admin", false);
        registerUser(admin);

		// Test customer authentication
        response = sendLoginRequest(customer);
        checkToken(response, customer);

		// Test admin authentication
        response = sendLoginRequest(admin);
        checkToken(response, admin);
	}

    /**
     * Test: does authentication fail with an invalid password?
     */
    @Test
    public void authenticationFailsWithIncorrectPassword() {
        Customer customer = new Customer(randomEmail(), "12345", "Jane", "Doe", new Date());
        registerUser(customer);

        // invalidate password
        customer.setPassword("invalid");

        ResponseEntity<Void> response = sendLoginRequest(customer);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

	/**
	 * Helper: save a user and return the saved user instance.
	 *
	 * @return User
	 */
	private User registerUser(User user) {
		String originalPassword = user.getPassword();
		user.setPassword(passwordEncoder.encode(originalPassword));
		userRepository.save(user);
		user.setPassword(originalPassword);
		return user;
	}

	/**
	 * Helper: send a login request and return the response.
	 */
	private ResponseEntity<Void> sendLoginRequest(User user) {
		return restTemplate.postForEntity("http://localhost:" + port + "/api/login", user, Void.class);
	}

    /**
     * Helper: check a response for a valid JWT.
     */
    private void checkToken(ResponseEntity<?> response, User user) {
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
