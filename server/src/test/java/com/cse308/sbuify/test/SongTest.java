package com.cse308.sbuify.test;

import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;

public class SongTest extends AuthenticatedTest {

    // todo: implement tests

    @Test
    public void testInitialized() {}

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";  // use the user sbuify+admin@gmail.com for all tests in this class request require admin role
    }

    @Override
    public String getPassword() {
        return "a";
    }
}
