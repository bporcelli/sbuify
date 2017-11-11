package com.cse308.sbuify.test;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.PlayQueue;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.playlist.Library;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserController;
import com.cse308.sbuify.user.UserRepository;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.cse308.sbuify.test.TestUtils.randomEmail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserController controller;

	/**
	 * Test: does registration succeed when a unique email is provided?
	 */
	@Test
	public void registrationSucceedsWithUniqueEmail() {
	    ResponseEntity<User> response;

	    // Test customer registration
		Date birthday = Date.from(Instant.now());
        User customer = new Customer(randomEmail(), "123", "John", "Doe", birthday);

        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Test admin registration
        Admin admin = new Admin(randomEmail(), "123", "Test", "Admin", false);

        response = sendRegisterRequest(admin);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

    /**
     * Test: does registration fail when an existing email is provided?
     */
    @Test
    public void registrationFailsWithDuplicateEmail() {
        ResponseEntity<User> response;

        Date birthday = Date.from(Instant.now());
        User customer = new Customer(randomEmail(), "123", "John", "Doe", birthday);

        // First attempt should succeed
        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Second attempt should fail
        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    /**
     * Test: is a customer's play queue and library created upon registration?
     */
    @Test
    public void customerInitializedOnRegistration() {
        ResponseEntity<User> response;

        // Test customer registration
        Date birthday = Date.from(Instant.now());
        User customer = new Customer(randomEmail(), "123", "John", "Doe", birthday);

        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get the new customer
        Optional<User> cust = userRepository.findByEmail(customer.getEmail());
        assert(cust.isPresent());
        Customer saved = (Customer) cust.get();

        // Ensure the customer's Play Queue and Library were created
        assertNotNull(saved.getPlayQueue());
        assertNotNull(saved.getLibrary());
    }

	/**
	 * Helper: send a registration request and return the response.
	 */
	private ResponseEntity<User> sendRegisterRequest(User user) {
		return restTemplate.postForEntity("http://localhost:" + port + "/api/users", user, User.class);
	}
    /**
     * Test Customer Registration Email Registration
     */
    @Test
    public void finishRegistrationEmail(){
        // Test customer registration

        Date birthday = Date.from(Instant.now());

        User customer = new Customer("SBUify@gmail.com", "123", "John", "Doe", birthday);

        ResponseEntity<User> response  = sendRegisterRequest(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Email customerRegistration = new NewAccountEmail(customer);

        assertEquals(customerRegistration.dispatch(), true);




    }
}
