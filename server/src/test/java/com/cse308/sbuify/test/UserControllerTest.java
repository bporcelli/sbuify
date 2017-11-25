package com.cse308.sbuify.test;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.cse308.sbuify.test.helper.AuthUtils.generateEmail;
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
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Test: does registration succeed when a unique email is provided?
     */
    @Test
    public void registrationSucceedsWithUniqueEmail() {
        ResponseEntity<User> response;

        // Test customer registration
        Date birthday = Date.from(Instant.now());
        User customer = new Customer(generateEmail(), "123", "John", "Doe", birthday);

        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Test admin registration
        Admin admin = new Admin(generateEmail(), "123", "Test", "Admin", false);

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
        User customer = new Customer(generateEmail(), "123", "John", "Doe", birthday);

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
        User customer = new Customer(generateEmail(), "123", "John", "Doe", birthday);

        response = sendRegisterRequest(customer);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get the new customer
        Optional<User> cust = userRepository.findByEmail(customer.getEmail());
        assert (cust.isPresent());
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
     * Test send password reset request
     */
    @Test
    public void sendPasswordResetRequest() {
        // use test customer "a" for test
        Optional<User> userOptional = userRepository.findByEmail("sbuify+a@gmail.com");
        assert(userOptional.isPresent());
        Customer customer = (Customer) userOptional.get();

        // send email as a POST parameter
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("email", customer.getEmail());

        // send reset request
        ResponseEntity<Void> response;
        response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/reset-password", params, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Test send password reset request with token
     */
    @Test
    public void changePasswordWithToken() {
        // send reset request
        sendPasswordResetRequest();

        // get user by email
        Optional<User> userOptional = userRepository.findByEmail("sbuify+a@gmail.com");
        assert(userOptional.isPresent());
        Customer customer = (Customer) userOptional.get();

        // send change password request
        String pass = "WhatPass";
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("token", customer.getToken());
        params.add("password", pass);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/change-password", params, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        userOptional = userRepository.findByEmail(customer.getEmail());
        customer = (Customer) userOptional.get();
        assertEquals(null, customer.getToken());
        assertEquals(true,passwordEncoder.matches(pass, customer.getPassword()));

        // reset password to old value
        customer.setPassword(passwordEncoder.encode("a"));
        userRepository.save(customer);
    }
}
