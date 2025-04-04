package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.exceptions.QrConfigurationException;

/**
 * Class to encode color values via "alpha red green blue"
 * <p>
 * Each of the four parts is stored in a byte so the complete color value has 32 bit.
 *
 * @see RgbaValue
 * @see RgbValue
 */
public class ArgbValue extends QrColor {

    /**
     * Create an instance representing the given color/alpha value
     *
     * @param alpha transparency of the color. Zero means 'completely transparent',
     *              255 (0xff) means 'opaque'
     * @param red   value for red; from 0 (no red) to 255 (0xff) (red as can be)
     * @param green value for green; from 0 (no green) to 255 (0xff) (green as can be)
     * @param blue  value for blue; from 0 (no blue) to 255 (0xff) (blue as can be)
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     */
    public ArgbValue(int alpha, int red, int green, int blue) throws QrConfigurationException {
        super(computeRawArgbVale(alpha, red, green, blue));
    }

    /**
     * Construct an instance from the raw 32bit value
     *
     * @param rawArgbValue the raw value for alpha (most significant byte), red, green
     *                     and blue (least significant byte)
     */
    public ArgbValue(int rawArgbValue) {
        super(rawArgbValue);
    }

    /**
     * Create a copy with the RGB values being scaled
     * <p>
     * Create a copy of this color value where the values of red, green and blue are
     * multiplied with a given factor. The alpha channels stays as is.
     *
     * @param factor the factor to be applied
     * @return a new instance with RGB being scaled
     */
    public ArgbValue scale(double factor) {
        return new ArgbValue(getScaledRawArgbValue(factor));
    }

    /**
     * Return a raw 32 bit ARGB value
     * <p>
     * The returned integer contains alpha (most significant byte), red, green
     * and blue (least significant byte) - each having one byte.
     *
     * @return a raw 32 bit ARGB value
     */
    @Override
    public int getRawArgbValue() {
        return super.getRawArgbValue();
    }

    @Override
    public String toString() {
        return String.format("#%08X", super.getRawArgbValue());
    }
}
