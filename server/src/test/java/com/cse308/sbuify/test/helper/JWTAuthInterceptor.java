package com.cse308.sbuify.test.helper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.cse308.sbuify.security.SecurityConstants.HEADER_NAME;

/**
 * HTTP request interceptor that takes care of attaching the user's JWT to every outgoing
 * client request.
 */
public class JWTAuthInterceptor implements ClientHttpRequestInterceptor {

    /** The current JWT, if any */
    private String JWT = null;

    /**
     * Intercept outgoing HTTP requests. If the user is authenticated, attach their JWT to the request.
     * Otherwise, authenticate them and continue processing the request.
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest,
                                        byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        if (JWT != null) {  // add JWT to request
            HttpHeaders headers = httpRequest.getHeaders();
            headers.add(HEADER_NAME, JWT);
        }

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);

        if (response.getStatusCode() != HttpStatus.FORBIDDEN) {  // save JWT sent in response
            HttpHeaders respHeaders = response.getHeaders();
            JWT = respHeaders.getFirst(HEADER_NAME);
        }
        return response;
    }
}
