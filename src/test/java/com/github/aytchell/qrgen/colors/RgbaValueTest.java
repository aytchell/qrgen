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
}
