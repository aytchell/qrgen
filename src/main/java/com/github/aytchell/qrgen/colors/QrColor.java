package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.awt.*;

/**
 * Base class for all color representations accepted by the QrGenerator
 */
@EqualsAndHashCode
public class QrColor {
    @Getter(value = AccessLevel.PROTECTED)
    private final int rawArgbValue;

    /**
     * Create an instance representing the given color/alpha value
     * @param rawArgbValue the raw value for alpha (most significant byte), red, green
     *                     and blue (least significant byte)
     */
    protected QrColor(int rawArgbValue) {
        this.rawArgbValue = rawArgbValue;
    }

    /**
     * Query whether the given color has an alpha channel or not
     * @return true if the color has an alpha channel; false otherwise
     */
    public boolean hasAlpha() {
        return (rawArgbValue >>> 24) != 0xff;
    }

    public int getAlpha() {
        return (rawArgbValue >>> 24);
    }

    /**
     * Return a java.awt.Color instance that corresponds to this argb instance
     * @return An awt Color instance which resembles this instance
     */
    public Color asAwtColor() {
        return new Color(rawArgbValue, hasAlpha());
    }

    protected static int computeRawArgbVale(int alpha, int red, int green, int blue) throws QrConfigurationException {
        if (alpha < 0 || alpha > 255)
            throw new QrConfigurationException("Given value for alpha is out of range [0, 255]");
        if (red   < 0 || red   > 255)
            throw new QrConfigurationException("Given value for red is out of range [0, 255]");
        if (green < 0 || green > 255)
            throw new QrConfigurationException("Given value for green is out of range [0, 255]");
        if (blue  < 0 || blue  > 255)
            throw new QrConfigurationException("Given value for blue is out of range [0, 255]");

        return (alpha << 24) | (red << 16) | (green <<  8) | blue;
    }

    protected int getScaledRawArgbValue(double factor) {
        final int newRed = scaleByte(2, factor);
        final int newGreen = scaleByte(1, factor);
        final int newBlue = scaleByte(0, factor);
        return ((getRawArgbValue() & 0xff000000) | newRed | newGreen | newBlue);
    }

    protected int getScaledRawRgbaValue(double factor) {
        final int newRed = scaleByte(2, factor);
        final int newGreen = scaleByte(1, factor);
        final int newBlue = scaleByte(0, factor);
        return (newRed | newGreen | newBlue | (getRawArgbValue() & 0xff000000));
    }

    private int scaleByte(int num, double factor) {
        double value = (getRawArgbValue() >>> (num * 8)) & 0xff;
        value *= factor;

        if (value > 255.0) return 255 << (num * 8);
        return (int)value << (num * 8);
    }

    public QrColor withoutAlpha() {
        if (!hasAlpha()) {
            return this;
        } else {
            return new QrColor(rawArgbValue | 0xff000000);
        }
    }
}
