package com.cse308.sbuify.test;

import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;

public class AdminControllerSuperAdminTest extends AuthenticatedTest {

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com"; // use the user sbuify+admin@gmail.com for all tests in this class request
                                         // require admin role
    }

    @Override
    public String getPassword() {
        return "a";
    }

    @Test
    public void testInitialized() {
        // included to prevent initializationError
    }
}
