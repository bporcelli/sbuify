package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cse308.sbuify.common.TypedCollection;
import com.cse308.sbuify.test.helper.AuthenticatedTest;

public class ReportTest extends AuthenticatedTest {
    @Test
    public void customQueryTest() {
        List<String> queryParam = new ArrayList<>();
        queryParam.add("1");

        HttpHeaders header = new HttpHeaders();
        header.add("id", "listeners-report");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/reports/generate/{id}",
                new TypedCollection(queryParam, String.class), String.class, header);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
