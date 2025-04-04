package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;

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

    public ArgbValue(int rawArgbValue) {
        super(rawArgbValue);
    }

    public ArgbValue scale(double factor) {
        return new ArgbValue(getScaledRawArgbValue(factor));
    }

    @Override
    public int getRawArgbValue() {
        return super.getRawArgbValue();
    }

    @Override
    public String toString() {
        return String.format("#%08X", super.getRawArgbValue());
    }
}
