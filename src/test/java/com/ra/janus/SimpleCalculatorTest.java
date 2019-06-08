package com.ra.janus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleCalculatorTest {
    private SimpleCalculator simpleCalculator = new SimpleCalculator();

    @Test
    void testSum() {
        Assertions.assertEquals(simpleCalculator.sum(10, 10), 20);
    }

    @Test
    void whenPassTwoNumbersThenSubtractionReturn() {
        Assertions.assertEquals(simpleCalculator.sub(10, 10), 0);
    }
}
