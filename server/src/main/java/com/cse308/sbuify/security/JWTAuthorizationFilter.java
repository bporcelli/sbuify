package com.cse308.sbuify.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_NAME;
import static com.cse308.sbuify.security.SecurityConstants.HEADER_PREFIX;
import static com.cse308.sbuify.security.SecurityConstants.SECRET;
import static java.util.Collections.emptyList;

/**
 * JWT Authorization Filter.
 *
 * Parses the Authorization header to get an Authentication token for an authenticated user.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
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

            UsernamePasswordAuthenticationToken token = getAuthenticationToken(header);

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
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String header) {
        String email = Jwts.parser()
                                .setSigningKey(SECRET.getBytes())
                                .parseClaimsJws(header.replace(HEADER_PREFIX, ""))
                                .getBody()
                                .getSubject();

        if (email != null) {
            // TODO: set GrantedAuthorities based on user role
            return new UsernamePasswordAuthenticationToken(email, null, emptyList());
        }

        return null;
    }

}
