package com.eliasnogueira.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FirstTest {

    @Test
    void testOne() {
        Assertions.assertTrue(true);
    }

    @Test
    void testTwo() {
        Assertions.assertTrue(true);
    }

    @Test
    void testThree() {
        Assertions.fail();
    }
}
