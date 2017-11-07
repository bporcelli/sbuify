package com.cse308.sbuify.test;

import java.time.Instant;

public class TestUtils {
    /**
     * Helper: generate a random email address.
     */
    public static String randomEmail() {
        Integer curTime = Instant.now().getNano();
        return Integer.toString(curTime) + "@test.com";
    }
}
