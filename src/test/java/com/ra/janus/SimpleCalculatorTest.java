package com.ra.janus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleCalculatorTest {
    private SimpleCalculator simpleCalculator = new SimpleCalculator();

    @Test
    public void testSum() {
        Assertions.assertEquals(simpleCalculator.sum(10, 10), 20);
    }
}
