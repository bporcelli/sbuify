package com.cse308.sbuify.test;

import com.cse308.sbuify.user.Admin;
import com.cse308.sbuify.user.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SBUifyApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

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

}
