package com.cse308.sbuify.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cse308.sbuify.security.SecurityConstants.*;

/**
 * JWT Authorization Filter.
 *
 * Parses the Authorization header to get an Authentication token for an authenticated user.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private UserDetailsServiceImpl userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    /**
     * Parses the Authorization header to obtain an Authentication token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_NAME);

        if (header != null && header.startsWith(HEADER_PREFIX)) {  // JWT provided
            Authentication token = getAuthenticationToken(header);

            // Provide token to security context
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        chain.doFilter(request, response);
    }

    /**
     * Parse the content of the Authorization header to obtain an Authentication token.
     *
     * If parsing fails, an exception will be thrown and handled by the default AccessDeniedHandler.
     */
    private Authentication getAuthenticationToken(String header) {
        Claims body = Jwts.parser()
                                .setSigningKey(SECRET.getBytes())
                                .parseClaimsJws(header.replace(HEADER_PREFIX, ""))
                                .getBody();

        String email = body.getSubject();

        if (email != null) {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            return new PreAuthenticatedAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        }
        return null;
    }

}
