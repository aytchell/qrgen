package com.github.aytchell.qrgen;

import lombok.EqualsAndHashCode;

/**
 * Class to convert color values into the integer format accepted by {@link QrGenerator#withColors(int, int)}
 */
@EqualsAndHashCode
public class ArgbValue {
    private final int rawValue;

    /**
     * Create an instance representing the given color/alpha value
     * @param alpha transparency of the color. Zero means 'completely transparent',
     *              255 (0xff) means 'opaque'
     * @param red value for red; from 0 (no red) to 255 (0xff) (red as can be)
     * @param green value for green; from 0 (no green) to 255 (0xff) (green as can be)
     * @param blue value for blue; from 0 (no blue) to 255 (0xff) (blue as can be)
     */
    public ArgbValue(int alpha, int red, int green, int blue) {
        rawValue =
                (toUnsignedByte(alpha) << 24) |
                        (toUnsignedByte(red) << 16) |
                        (toUnsignedByte(green) <<  8) |
                        toUnsignedByte(blue);
    }

    /**
     * Create an instance representing the given color/alpha value
     * @param rawArgbValue the raw value for alpha (most significant byte), red, green
     *                     and blue (least significant byte)
     * @see ArgbValue#ArgbValue(int, int, int, int)
     */
    public ArgbValue(int rawArgbValue) {
        rawValue = rawArgbValue;
    }

    /**
     * Query whether the given color has an alpha channel or not
     * @return true if the color has an alpha channel; false otherwise
     */
    public boolean hasAlpha() {
        return (rawValue >>> 24) != 0xff;
    }

    /**
     * Get the raw integer representation of the given color value
     * @return An integer where each byte represents one of the channels
     *      (from highest to lowest) "alpha", "red", "green" and "blue"
     */
    public int getRawValue() {
        return rawValue;
    }

    private int toUnsignedByte(int value) {
        if (value < 0) return 0;
        return Math.min(value, 255);
    }
}
