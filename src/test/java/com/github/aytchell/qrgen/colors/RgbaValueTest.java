package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RgbaValueTest {
    @Test
    public void constructorWorksAsExpected() throws QrConfigurationException {
        final RgbaValue actual = new RgbaValue(0x10, 0x20, 0x30, 0x40);
        assertTrue(actual.hasAlpha());
        assertEquals(0x10, actual.asAwtColor().getRed());
        assertEquals(0x20, actual.asAwtColor().getGreen());
        assertEquals(0x30, actual.asAwtColor().getBlue());
        assertEquals(0x40, actual.asAwtColor().getAlpha());
    }

    @Test
    public void rawRgbaValueCanBeComputed() throws QrConfigurationException {
        assertEquals(0x12345678, new RgbaValue(0x12, 0x34, 0x56, 0x78).getRawRgbaValue());
        assertEquals(0xf0345678, new RgbaValue(0xf0, 0x34, 0x56, 0x78).getRawRgbaValue());
    }

    @Test
    public void toStringReturnsHexRepresentation() throws QrConfigurationException {
        assertEquals("#12345678", new RgbaValue(0x12, 0x34, 0x56, 0x78).toString());
        assertEquals("#ABCDEF00", new RgbaValue(0xab, 0xcd, 0xef, 0x00).toString());
    }
}
