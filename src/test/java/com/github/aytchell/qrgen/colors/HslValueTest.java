package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.exceptions.QrConfigurationException;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

// most of the hsl related tests are done in HslaValueTest;
// here is only one test to check that the forwarding of hsl values and transparency works
public class HslValueTest {
    @Test
    public void simpleHslValueCanBeCreated() throws QrConfigurationException {
        final HslValue actual = new HslValue(348, 53, 55);
        assertFalse(actual.hasAlpha());

        final Color awtColor = actual.asAwtColor();
        assertEquals(0xc9, awtColor.getRed());
        assertEquals(0x4f, awtColor.getGreen());
        assertEquals(0x68, awtColor.getBlue());
        assertEquals(0xff, awtColor.getAlpha());
    }
}
