package com.cse308.sbuify.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.cse308.sbuify.security.SecurityConstants.*;

/**
 * Spring Security configuration.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Customize the default web security configuration.
     *
     * Only a basic security policy is enforced at the web-application level. In general, method
     * security should be used to control access.
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
                    // sign up, login, reset pass, and change pass are publicly accessible
                    .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                    .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                    .antMatchers(HttpMethod.POST, RESET_URL).permitAll()
                    .antMatchers(HttpMethod.POST, CHANGE_PASS_URL).permitAll()
                    // static images are publicly accessible
                    .antMatchers(HttpMethod.GET, IMAGE_PATTERN).permitAll()
                    // the stream endpoint is publicly accessible (access controls in stream method)
                    .antMatchers(HttpMethod.GET, STREAM_PATTERN).permitAll()
                    // admin endpoints are only accessible to admins
                    .antMatchers(ADMIN_PATTERN).hasRole("ADMIN")
                    // customer endpoints are only accessible to customers
                    .antMatchers(CUSTOMER_PATTERN).hasRole("CUSTOMER")
                    // cron endpoints are only accessible by localhost
                    .antMatchers(CRON_PATTERN).hasIpAddress("localhost")
                    // by default, endpoints are only accessible to authenticated users
                    .anyRequest().authenticated()
                    .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService))
                .sessionManagement()
                    // disable session creation
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
