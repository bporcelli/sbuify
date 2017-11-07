package com.cse308.sbuify.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserController;
import com.cse308.sbuify.user.UserRepository;

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

	@Autowired
	private UserController controller;
	
	@Test
	public void contexLoads() throws Exception {
		assertNotNull(controller);
	}
	
	/**
	 * Helper: send a registration request and return the response.
	 */
	private ResponseEntity<Void> sendRegisterRequest(User user) {
		return restTemplate.postForEntity("http://localhost:" + port + "/api/users", user, Void.class);
	}
}
