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

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserController;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.xml.ws.http.HTTPBinding;
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
     * Test Customer Registration Email Registration
     */
    @Test
    public void finishRegistrationEmail() {
        // Test customer registration

        Date birthday = Date.from(Instant.now());

        User customer = new Customer(generateEmail(), "123", "John", "Doe", birthday);

        ResponseEntity<User> response = sendRegisterRequest(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Email customerRegistration = new NewAccountEmail(customer);

        assertEquals(true, customerRegistration.dispatch());

        // Get the new customer
        Optional<User> cust = userRepository.findByEmail(customer.getEmail());
        assert (cust.isPresent());
        Customer saved = (Customer) cust.get();

        // Ensure the customer's Play Queue and Library were created
        assertNotNull(saved.getPlayQueue());
        assertNotNull(saved.getLibrary());
    }
    /*
     * Test send password reset request
     */
    @Test
    public void sendPasswordResetRequest() {
        // Test customer registration
        Date birthday = Date.from(Instant.now());
        User customer = new Customer("sbuify@gmail.com", "123", "John", "Doe", birthday);

        ResponseEntity<?> response = sendRegisterRequest(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get the new customer
        Optional<User> cust = userRepository.findByEmail(customer.getEmail());
        assert (cust.isPresent());
        Customer saved = (Customer) cust.get();

        // Ensure the customer's Play Queue and Library were created
        assertNotNull(saved.getPlayQueue());
        assertNotNull(saved.getLibrary());

        // now reset request

        response = restTemplate.postForEntity("http://localhost:" + port + "/api/reset-password", saved.getEmail() , String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*
    * Test send password reset request with token
    */
    @Test
    public void changePasswordWithToken() {
        // Test customer registration
        Date birthday = Date.from(Instant.now());
        User customer = new Customer("sbuify@gmail.com", "123", "John", "Doe", birthday);


        ResponseEntity<?> response = sendRegisterRequest(customer);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Get the new customer
        Optional<User> cust = userRepository.findByEmail(customer.getEmail());
        assert (cust.isPresent());
        Customer saved = (Customer) cust.get();

        // Ensure the customer's Play Queue and Library were created
        assertNotNull(saved.getPlayQueue());
        assertNotNull(saved.getLibrary());

        // now reset request

        response = restTemplate.postForEntity("http://localhost:" + port + "/api/reset-password", saved.getEmail() , String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());


        // use token to get user

        cust = userRepository.findByEmail(customer.getEmail());
        assert (cust.isPresent());
        saved = (Customer) cust.get();
        String pass = "WhatPass";
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("token", saved.getToken());
        params.add("password", pass);

        response = restTemplate.postForEntity("http://localhost:" + port + "/api/reset-password/", params , String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        cust = userRepository.findByEmail(customer.getEmail());
        saved = (Customer) cust.get();
        assertEquals(null, saved.getToken());
        assertEquals(true,passwordEncoder.matches(pass,saved.getPassword()));






    }







}
