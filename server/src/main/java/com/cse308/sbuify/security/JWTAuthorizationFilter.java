package com.cse308.sbuify.security;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_NAME;
import static com.cse308.sbuify.security.SecurityConstants.HEADER_PREFIX;
import static com.cse308.sbuify.security.SecurityConstants.SECRET;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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
        Claims body = Jwts.parser()
                                .setSigningKey(SECRET.getBytes())
                                .parseClaimsJws(header.replace(HEADER_PREFIX, ""))
                                .getBody();

        String email = body.getSubject();

        if (email != null) {
            // TODO: NEED TO SUPPLY AUTHORITIES HERE?
            List<GrantedAuthority> scopes = new ArrayList<>();

            for (String str: (ArrayList<String>) body.get("scopes")) {
                scopes.add(new SimpleGrantedAuthority(str));
            }

            return new UsernamePasswordAuthenticationToken(email, null, scopes);
        }

        return null;
    }

}
