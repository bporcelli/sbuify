package com.cse308.sbuify.security;

import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cse308.sbuify.security.SecurityConstants.*;
import static java.util.Collections.emptyList;

/**
 * JWT Authentication Filter.
 *
 * Processes authentication requests.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    /**
     * Constructor. Calls setFilterProcessesUrl to change the authentication endpoint to /api/login.
     */
    public JWTAuthenticationFilter(AuthenticationManager authManager) {
        this.authManager = authManager;

        setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
    }

    /**
     * Performs authentication by passing an Authentication token on to the AuthenticationManager.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            // Parse request to extract User object
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            // Construct Authentication token using credentials
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),
                                                                                                user.getPassword(),
                                                                                                emptyList());

            // Authenticate with authentication manager
            return authManager.authenticate(token);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Issue JWT on successful authentication.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();

        // Build JWT with JJWT
        // TODO: what other claims should be included?
        String token = Jwts.builder()
                                .setSubject(principal.getUsername())
                                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                                .compact();

        // Put token in response headers
        response.addHeader(HEADER_NAME, HEADER_PREFIX + token);
    }
}
