package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;
import lombok.Value;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HslaValueTest {
    public static Stream<HslaToRgba> getHslaToRgbaTestData() throws QrConfigurationException {
        return Stream.of(
                new HslaToRgba(
                        new HslaValue(244, 43, 22, 0xf0), true,
                        0x23, 0x20, 0x50, 0xf0),

                new HslaToRgba(
                        new HslaValue(135, 42, 46, 0xd1), true,
                        0x44, 0xa7, 0x5d, 0xd1),

                new HslaToRgba(
                        new HslaValue (326, 30, 74,0x54), true,
                        0xd1, 0xa9, 0xbf, 0x54),

                new HslaToRgba(
                        new HslaValue(0, 0, 0, 0), true,
                        0x00, 0x00, 0x00, 0x00),

                new HslaToRgba(
                        new HslaValue(360, 100, 100, 255), false,
                        0xff, 0xff, 0xff, 0xff),

                new HslaToRgba(
                        new HslaValue(10, 10, 10, 0.5), true,
                        0x1c, 0x18, 0x17, 128)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "getHslaToRgbaTestData")
    public void createValidHslaValues(HslaToRgba data) {
        assertEquals(data.hasAlpha, data.hsla.hasAlpha());

        final Color awtColor = data.hsla.asAwtColor();
        assertEquals(data.red, awtColor.getRed(), "red value");
        assertEquals(data.green, awtColor.getGreen(), "green value");
        assertEquals(data.blue, awtColor.getBlue(), "blue value");
        assertEquals(data.alpha, awtColor.getAlpha(), "alpha value");
    }

    public static Stream<RawHsla> getBrokenHslaValues() {
        return Stream.of(
                new RawHsla(-123, 45, 65, 176),
                new RawHsla(361, 45, 65, 176),
                new RawHsla(123, -45, 65, 176),
                new RawHsla(123, 101, 65, 176),
                new RawHsla(123, 45, -65, 176),
                new RawHsla(123, 45, 101, 176),
                new RawHsla(123, 45, 65, -176),
                new RawHsla(123, 45, 65, 256)
        );
    }

    @ParameterizedTest
    @MethodSource(value = "getBrokenHslaValues")
    public void constructorWillThrowOnIllegalInput(RawHsla data) {
        assertThrows(QrConfigurationException.class,
                () -> new HslaValue(data.hue, data.saturation, data.lightness, data.alpha));
    }

    @Test
    public void toStringReturnsNiceRepresentation() throws QrConfigurationException {
        assertEquals("(320°, 34%, 56%, 240)", new HslaValue(320, 34, 56, 240).toString());
        assertEquals("(12°, 12%, 98%, 0)", new HslaValue(12, 12, 98, 0).toString());
    }

    @Value
    private static class HslaToRgba {
        HslaValue hsla;
        boolean hasAlpha;
        int red;
        int green;
        int blue;
        int alpha;
    }

    @Value
    private static class RawHsla {
        int hue;
        int saturation;
        int lightness;
        int alpha;
    }
}
