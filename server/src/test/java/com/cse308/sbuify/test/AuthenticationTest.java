package com.cse308.sbuify.test;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.test.helper.AuthUtils;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Optional;

import static com.cse308.sbuify.security.SecurityConstants.*;
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


	/**
	 * Test: does authentication succeed when a correct password is provided?
	 */
	@Test
	public void authenticationSucceedsWithCorrectPassword() {
        ResponseEntity<Void> response;

        // Three test customers are available: 'a', 'b', and 'c' with matching passwords
		Optional<User> uA = userRepository.findByEmail("sbuify+a@gmail.com");
		assert(uA.isPresent());
		Customer customer = (Customer) uA.get();
		customer.setPassword("a");

		// One test admin is available: 'admin' with password 'a'
		Optional<User> uAdmin = userRepository.findByEmail("sbuify+admin@gmail.com");
		assert(uAdmin.isPresent());
		Admin admin = (Admin) uAdmin.get();
		admin.setPassword("a");

		// Test customer authentication
        response = AuthUtils.sendLoginRequest(port, restTemplate, customer);
        AuthUtils.checkToken(response, customer);

		// Test admin authentication
        response = AuthUtils.sendLoginRequest(port, restTemplate, admin);
        AuthUtils.checkToken(response, admin);
	}

    /**
     * Test: does authentication fail with an invalid password?
     */
    @Test
    public void authenticationFailsWithIncorrectPassword() {
		Optional<User> uB = userRepository.findByEmail("sbuify+b@gmail.com");
		assert(uB.isPresent());
		Customer customer = (Customer) uB.get();

        // invalidate password
        customer.setPassword("invalid");

        ResponseEntity<Void> response = AuthUtils.sendLoginRequest(port, restTemplate, customer);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
