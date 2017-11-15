package com.cse308.sbuify.test.helper;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_NAME;
import static com.cse308.sbuify.security.SecurityConstants.HEADER_PREFIX;
import static com.cse308.sbuify.security.SecurityConstants.SECRET;
import static com.cse308.sbuify.test.TestUtils.randomEmail;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class LoginHelper {
	/**
	 * Helper: save a user and return the saved user instance.
	 *
	 * @return User
	 */
	public static User registerUser(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, User user) {
		String originalPassword = user.getPassword();
		user.setPassword(passwordEncoder.encode(originalPassword));
		userRepository.save(user);
		user.setPassword(originalPassword);
		return user;
	}

	/**
	 * Helper: send a login request and return the response.
	 */
	public static ResponseEntity<Void> sendLoginRequest(int port, TestRestTemplate restTemplate, User user) {
		return restTemplate.postForEntity("http://localhost:" + port + "/api/login", user, Void.class);
	}

	/**
	 * Helper: check a response for a valid JWT.
	 */
	public static void checkToken(ResponseEntity<?> response, User user) {
		String JWT = response.getHeaders().getFirst(HEADER_NAME);

		Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(JWT.replace(HEADER_PREFIX, ""))
				.getBody();

		String email = claims.getSubject();
		ArrayList<String> scopes = (ArrayList<String>) claims.get("scopes");

		boolean emailMatches = user.getEmail().equals(email);
		boolean roleMatches = scopes.equals(SecurityUtils.getAuthorityStrings(user));
		user.setToken(JWT);

		assert (emailMatches && roleMatches);
	}
	
	/**
	 * Helper: Simulate registration and login
	 */
	public static Customer simulateCustomerRegisterLogin(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository, int port, TestRestTemplate restTemplate) {
	    Customer customer = new Customer(randomEmail(), "12345", "Jane", "Doe", new Date());
        LoginHelper.registerUser(passwordEncoder, userRepository, customer);
        
        ResponseEntity<?> response = LoginHelper.sendLoginRequest(port, restTemplate, customer);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginHelper.checkToken(response, customer);
        
        return customer;
	}
}
