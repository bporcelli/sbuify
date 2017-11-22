package com.cse308.sbuify.test;

import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SongTest extends AuthenticatedTest {

	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * Test: is search song work properly?
	 */
     @Test
	public void postCustomerSong() {
         // todo: update to use demo data
         // prepare song to send
//         Song toSend = new Song();
//         toSend.setLength(30000);
//
//         System.out.println(toSend);
//
//         ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:" + port + "/api/customer/songs", toSend,
//				Void.class);
//
//         assertEquals(HttpStatus.OK, response.getStatusCode());
	}

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";  // use the user sbuify+b@gmail.com for all tests in this class
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
