package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayDeque;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.PlayQueue;
import com.cse308.sbuify.security.SecurityConstants;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.test.helper.LoginHelper;
import com.cse308.sbuify.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlayQueueTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Test: is search song work properly?
	 */
	@Test
	public void putPlayQueue() {
	    // simulate login
	    Customer customer = LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);

		// set appropriate headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(SecurityConstants.HEADER_NAME, SecurityConstants.HEADER_PREFIX + customer.getToken());
		
		// create PlayQueue to send
		Collection<Song> toSend = new ArrayDeque<>();
		
		toSend.add(new Song());
		toSend.add(new Song());
		toSend.add(new Song());
		
		// create request object
		HttpEntity<Collection<Song>> request = new HttpEntity<>(toSend, headers);
		
		System.out.println(request);
		
		// send the request
		ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/customer/play-queue", HttpMethod.PUT, request,
				Void.class);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
//	@Test
//    public void putEmptyPlayQueue() {
//        // simulate login
//        Customer customer = LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
//
//        // set appropriate headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add(SecurityConstants.HEADER_NAME, SecurityConstants.HEADER_PREFIX + customer.getToken());
//        
//        // create PlayQueue to send
//        Collection<Song> toSend = new ArrayDeque<>();
//        
//        System.out.println(toSend);
//        
//        // create request object
//        HttpEntity<Collection<Song>> request = new HttpEntity<>(toSend, headers);
//        
//        // send the request
//        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/api/customer/play-queue", HttpMethod.PUT, request,
//                Void.class);
//        
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
}
