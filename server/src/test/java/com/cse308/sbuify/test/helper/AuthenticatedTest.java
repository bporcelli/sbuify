package com.cse308.sbuify.test.helper;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Optional;

/**
 * Subclassed by tests that require authenticated users.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthenticatedTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected UserRepository userRepository;

    protected TestRestTemplate restTemplate;

    protected User user;

    /**
     * Register the JWTAuthInterceptor and authenticate the user when the TestRestTemplate is injected.
     */
    @Autowired
    public void setRestTemplate(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        restTemplate.getRestTemplate().setInterceptors(Arrays.asList(new JWTAuthInterceptor()));

        Optional<User> userOptional = userRepository.findByEmail(getEmail());
        assert(userOptional.isPresent());
        user = userOptional.get();

        String encryptedPass = user.getPassword();
        user.setPassword(getPassword());
        AuthUtils.sendLoginRequest(port, restTemplate, user);
        user.setPassword(encryptedPass);
    }

    /**
     * Email to use for tests.
     */
    public abstract String getEmail();

    /**
     * Password to use for tests.
     */
    public abstract String getPassword();
}
