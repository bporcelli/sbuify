package com.cse308.sbuify.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.cse308.sbuify.security.SecurityConstants.LOGIN_URL;
import static com.cse308.sbuify.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Customize the default web security configuration.
     *
     * Permit all POST requests to LOGIN_URL and SIGN_UP_URL and require authentication for all other requests.
     *
     * Disable session creation.
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: configure CSRF/CORS
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                    .anyRequest().authenticated()
                    .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * Configure the UserDetailsService implementation and password encoder used by the AuthenticationManager.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
