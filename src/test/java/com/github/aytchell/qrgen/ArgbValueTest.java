package com.github.aytchell.qrgen;

import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArgbValueTest {

    private static Stream<ColorsAndAlpha> alphaTestColors() {
        final List<ColorsAndAlpha> data = new ArrayList<>();
        data.add(new ColorsAndAlpha(new ArgbValue(0, 255, 255, 255), true));
        data.add(new ColorsAndAlpha(new ArgbValue(100, 100, 100, 100), true));
        data.add(new ColorsAndAlpha(new ArgbValue(255, 0, 0, 255), false));
        data.add(new ColorsAndAlpha(new ArgbValue(255, 255, 255, 255), false));
        return data.stream();
    }

    @ParameterizedTest
    @MethodSource("alphaTestColors")
    void hasAlphaWorksAsExpected(ColorsAndAlpha data) {
        final boolean expectation = data.isAlpha();
        assertEquals(expectation, data.getColor().hasAlpha());
    }

    @Test
    void scaleToHalfWorks() {
        assertEquals(
                new ArgbValue(255, 120, 80, 60),
                new ArgbValue(255, 240, 160, 120).scale(0.5)
        );
    }

    @Test
    void scaleToDoubleWorks() {
        assertEquals(
                new ArgbValue(255, 60, 255, 180),
                new ArgbValue(255, 30, 180, 90).scale(2)
        );
    }

    @Value
    private static class ColorsAndAlpha {
        ArgbValue color;
        boolean alpha;
    }
}
