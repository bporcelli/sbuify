package com.cse308.sbuify.test;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.PlayQueue;
import com.cse308.sbuify.playlist.Library;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.user.AppUser;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static com.cse308.sbuify.security.SecurityConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Login & registration tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Test Customer registration.
    */
    @Test
	public void customerRegistrationSucceeds() {
        DateFormat format = new SimpleDateFormat("MM/dd/YYYY");

        try {
            Date birthday = format.parse("06/04/1996");

            AppUser customer = new Customer("test.email@test.com", "123", "John", "Doe",
                                         birthday);

            ResponseEntity<Void> response = sendRegisterRequest(customer);

            // 201 response expected
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            // Get the new customer
            Optional<AppUser> savedCust = userRepository.findByEmail(customer.getEmail());
            Customer saved = (Customer) savedCust.get();

            // Ensure the customer's Play Queue and Library were created
            PlayQueue playQueue = saved.getPlayQueue();
            Library library = saved.getLibrary();

            assertNotNull(playQueue);
            assertNotNull(library);

            // If we try again with the same email, we should get a CONFLICT response
            response = sendRegisterRequest(customer);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        } catch (ParseException ex) {
            System.out.println("Failed to parse date:" + ex.getMessage());
        }
    }

    /**
     * Test Admin registration.
     */
    @Test
    public void adminRegistrationSucceeds() {
        Admin admin = new Admin("test.admin@test.com", "123", "Test",
                                "Admin", false);

        ResponseEntity<Void> response = sendRegisterRequest(admin);

        // Expect success
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Test Customer authentication.
     */
    @Test
    public void customerAuthenticationSucceeds() {
        // Create user
        Customer dummyCust = new Customer("test.customer@test.com", "12345", "Jane",
                                         "Doe", new Date());

        registerUser(dummyCust);

        // Authentication should succeed with a valid password
        ResponseEntity<Void> response = sendLoginRequest(dummyCust);

        assert(tokenValid(response.getHeaders().getFirst(HEADER_NAME), dummyCust));

        // Authentication should fail with an invalid password
        dummyCust.setPassword("invalid");

        response = sendLoginRequest(dummyCust);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /**
     * Test Admin authentication.
     */
    @Test
    public void adminAuthenticationSucceeds() {
        // Create admin
        Admin admin = new Admin("test.admin@gmail.com", "123", "Test", "Admin",
                                false);
        registerUser(admin);

        // Attempt authentication
        ResponseEntity<Void> response = sendLoginRequest(admin);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assert(tokenValid(response.getHeaders().getFirst(HEADER_NAME), admin));
    }

    /**
     * Helper: save a user and return the saved user instance.
     *
     * @return AppUser
     */
    private AppUser registerUser(AppUser user) {
        String originalPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(originalPassword));
        userRepository.save(user);
        user.setPassword(originalPassword);
        return user;
    }

    /**
     * Helper: send a registration request and return the response.
     */
    private ResponseEntity<Void> sendRegisterRequest(AppUser user) {
        return restTemplate.postForEntity("http://localhost:" + port + "/api/users",
                                          user, Void.class);
    }

    /**
     * Helper: send a login request and return the response.
     */
    private ResponseEntity<Void> sendLoginRequest(AppUser user) {
        return restTemplate.postForEntity("http://localhost:" + port + "/api/login",
                                          user, Void.class);
    }

    /**
     * Helper: check whether token is valid for user.
     */
    private boolean tokenValid(String token, AppUser user) {
        Claims claims = Jwts.parser()
                                .setSigningKey(SECRET.getBytes())
                                .parseClaimsJws(token.replace(HEADER_PREFIX, ""))
                                .getBody();

        String email = claims.getSubject();
        ArrayList<String> scopes = (ArrayList<String>) claims.get("scopes");

        boolean emailMatches = user.getEmail().equals(email);
        boolean roleMatches = scopes.equals(SecurityUtils.getAuthorityStrings(user));

        return emailMatches && roleMatches;
    }
}
