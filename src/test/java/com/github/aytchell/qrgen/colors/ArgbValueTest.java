package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ArgbValueTest {

    private static Stream<ColorsAndAlpha> alphaTestColors() throws QrConfigurationException {
        final List<ColorsAndAlpha> data = new ArrayList<>();
        data.add(new ColorsAndAlpha(new ArgbValue(0, 255, 255, 255), true));
        data.add(new ColorsAndAlpha(new ArgbValue(100, 100, 100, 100), true));
        data.add(new ColorsAndAlpha(new ArgbValue(255, 0, 0, 255), false));
        data.add(new ColorsAndAlpha(new ArgbValue(255, 255, 255, 255), false));
        return data.stream();
    }

    @Test
    public void constructorWorksAsExpected() throws QrConfigurationException {
        final ArgbValue actual = new ArgbValue(0x10, 0x20, 0x30, 0x40);
        assertTrue(actual.hasAlpha());
        assertEquals(0x10, actual.asAwtColor().getAlpha());
        assertEquals(0x20, actual.asAwtColor().getRed());
        assertEquals(0x30, actual.asAwtColor().getGreen());
        assertEquals(0x40, actual.asAwtColor().getBlue());
    }

    @ParameterizedTest
    @MethodSource("alphaTestColors")
    void hasAlphaWorksAsExpected(ColorsAndAlpha data) {
        final boolean expectation = data.isAlpha();
        assertEquals(expectation, data.getColor().hasAlpha());
    }

    @Test
    void scaleToHalfWorks() throws QrConfigurationException {
        assertEquals(
                new ArgbValue(255, 120, 80, 60),
                new ArgbValue(255, 240, 160, 120).scale(0.5)
        );
    }

    @Test
    void scaleToDoubleWorks() throws QrConfigurationException {
        assertEquals(
                new ArgbValue(255, 60, 255, 180),
                new ArgbValue(255, 30, 180, 90).scale(2)
        );
    }

    @Test
    void creatingAnArgbValueFromIllegalValuesWillThrow() {
        assertThrows(QrConfigurationException.class, () -> new ArgbValue(-12, 45, 37, 45));
        assertThrows(QrConfigurationException.class, () -> new ArgbValue(276, 45, 37, 45));

        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, -45, 37, 45));
        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, 341, 37, 45));

        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, 45, -37, 45));
        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, 45, 256, 45));

        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, 45, 37, -45));
        assertThrows(QrConfigurationException.class, () -> new ArgbValue(12, 45, 37, 541));
    }

    @Test
    public void rawArgbValueCanBeComputed() throws QrConfigurationException {
        assertEquals(0x12345678, new ArgbValue(0x12, 0x34, 0x56, 0x78).getRawArgbValue());
        assertEquals(0xf0345678, new ArgbValue(0xf0, 0x34, 0x56, 0x78).getRawArgbValue());
    }

    @Test
    public void toStringReturnsHexRepresentation() throws QrConfigurationException {
        assertEquals("#12345678", new ArgbValue(0x12, 0x34, 0x56, 0x78).toString());
        assertEquals("#ABCDEF00", new ArgbValue(0xab, 0xcd, 0xef, 0x00).toString());
    }

    @Test
    public void withoutAlphaReturnsCopyWithoutAlphaChannel() throws QrConfigurationException {
        final ArgbValue value = new ArgbValue(23, 34, 45, 56);
        final QrColor noAlpha = value.withoutAlpha();

        assertTrue(value.hasAlpha());
        assertFalse(noAlpha.hasAlpha());

        final ArgbValue expectation = new RgbValue(34, 45, 56);
        assertEquals(expectation.getRawArgbValue(), noAlpha.getRawArgbValue());
    }

    @Value
    private static class ColorsAndAlpha {
        ArgbValue color;
        boolean alpha;
    }
}
