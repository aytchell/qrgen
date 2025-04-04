package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.exceptions.QrConfigurationException;

public class RgbaValue extends QrColor {

    /**
     * Create an instance representing the given color/alpha value
     *
     * @param red   value for red; from 0 (no red) to 255 (0xff) (red as can be)
     * @param green value for green; from 0 (no green) to 255 (0xff) (green as can be)
     * @param blue  value for blue; from 0 (no blue) to 255 (0xff) (blue as can be)
     * @param alpha transparency of the color. Zero means 'completely transparent',
     *              255 (0xff) means 'opaque'
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     */
    public RgbaValue(int red, int green, int blue, int alpha) throws QrConfigurationException {
        super(computeRawArgbVale(alpha, red, green, blue));
    }

    public RgbaValue(int rawRgbaValue) {
        super(convertToArgb(rawRgbaValue));
    }

    private static int convertToRgba(int rawArgb) {
        final int alpha = (rawArgb >>> 24);
        return (rawArgb << 8) | alpha;
    }

    private static int convertToArgb(int rawRgbaValue) {
        final int alpha = (rawRgbaValue & 0xff);
        return (alpha << 24) | (rawRgbaValue >>> 8);
    }

    public RgbaValue scale(double factor) {
        return new RgbaValue(getScaledRawRgbaValue(factor));
    }

    @Override
    public String toString() {
        return String.format("#%08X", getRawRgbaValue());
    }

    public int getRawRgbaValue() {
        int rawArgb = getRawArgbValue();
        return convertToRgba(rawArgb);
    }
}
