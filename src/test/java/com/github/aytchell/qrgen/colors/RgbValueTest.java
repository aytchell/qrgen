package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RgbValueTest {
    @Test
    void constructFromCorrectRawValueIsPossible() throws QrConfigurationException {
        final RgbValue value = new RgbValue(0x112233);

        assertFalse(value.hasAlpha());
        assertEquals(0xff112233, value.getRawArgbValue());
    }

    @Test
    void constructFromTripleIsPossible() throws QrConfigurationException {
        final RgbValue value = new RgbValue(0x34, 0x45, 0x56);

        assertFalse(value.hasAlpha());
        assertEquals(0xff344556, value.getRawArgbValue());
    }

    @Test
    void constructFromTooBigRawValueWillThrow() {
        assertThrows(
                QrConfigurationException.class,
                () -> new RgbValue(0x11223344)
        );
    }
}
