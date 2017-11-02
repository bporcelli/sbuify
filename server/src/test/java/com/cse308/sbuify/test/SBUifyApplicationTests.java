package com.cse308.sbuify.test;

import com.cse308.sbuify.user.Admin;
import com.cse308.sbuify.user.Customer;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.BeforeClass;
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
import java.util.Date;
import java.util.Optional;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_NAME;
import static com.cse308.sbuify.security.SecurityConstants.HEADER_PREFIX;
import static com.cse308.sbuify.security.SecurityConstants.SECRET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SBUifyApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

	@Test
	public void contextLoads() {
	    assertThat(restTemplate).isNotNull();
	}

    /**
     * Test Customer registration.
    */
    @Test
	public void customerRegistrationSucceeds() {
        DateFormat format = new SimpleDateFormat("MM/dd/YYYY");

        try {
            Date birthday = format.parse("06/04/1996");

            Customer customer = new Customer("test.email@test.com", "123", "John",
                                             "Doe", birthday);

            ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/api/users",
                                                                        customer, Void.class);

            // Expect success
            assertEquals(HttpStatus.CREATED, response.getStatusCode());

            // If we try again with the same email, we should get a CONFLICT response
            response = restTemplate.postForEntity("http://localhost:" + port + "/api/users",
                                                    customer, Void.class);

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

        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/api/users",
                                                                    admin, Void.class);

        // Expect success
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Test user authentication.
     */
    @Test
    public void authenticationSucceeds() {
        // Create user
        Customer dummyCust = new Customer("test.customer@test.com", "12345", "Jane",
                                         "Doe", null);

        dummyCust.setPassword(passwordEncoder.encode(dummyCust.getPassword()));

        userRepository.save(dummyCust);

        // Reset password to plaintext (expected during auth)
        dummyCust.setPassword("12345");

        // Attempt authentication with correct password
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/api/login",
                                                                   dummyCust, Void.class);

        String token = Jwts.builder()
                                .setSubject(dummyCust.getEmail())
                                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                                .compact();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HEADER_PREFIX + token, response.getHeaders().getFirst(HEADER_NAME));

        // Attempt again with invalid password
        dummyCust.setPassword("invalid");

        response = restTemplate.postForEntity("http://localhost:" + port + "/api/login",
                                               dummyCust, Void.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

}
