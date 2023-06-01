package com.github.aytchell.qrgen;

import lombok.EqualsAndHashCode;

import java.awt.*;

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
     * @throws QrConfigurationException  is thrown in case one of the values is out of bounds
     */
    public ArgbValue(int alpha, int red, int green, int blue) throws QrConfigurationException {
        if (alpha < 0 || alpha > 255 ||
                red   < 0 || red   > 255 ||
                green < 0 || green > 255 ||
                blue  < 0 || blue  > 255)
            throw new QrConfigurationException("Illegal color code given");

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
     * Return a java.awt.Color instance that corresponds to this argb instance
     * @return An awt Color instance which resembles this instance
     */
    public Color asAwtColor() {
        return new Color(rawValue, hasAlpha());
    }

    private int toUnsignedByte(int value) {
        if (value < 0) return 0;
        return Math.min(value, 255);
    }

    public ArgbValue scale(double factor) {
        final int newRed = scaleByte(2, factor);
        final int newGreen = scaleByte(1, factor);
        final int newBlue = scaleByte(0, factor);
        return new ArgbValue((rawValue & 0xff000000) | newRed | newGreen | newBlue);
    }

    private int scaleByte(int num, double factor) {
        double value = (rawValue >>> (num * 8)) & 0xff;
        value *= factor;

        if (value > 255.0) return 255 << (num * 8);
        return (int)value << (num * 8);
    }

    @Override
    public String toString() {
        return String.format("#%08X", rawValue);
    }
}
