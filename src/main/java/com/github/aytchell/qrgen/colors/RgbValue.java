package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.exceptions.QrConfigurationException;

/**
 * Class to encode color values via "red green blue"
 * <p>
 * Basically an {@link ArgbValue} with an alpha channel set to "opaque"
 *
 * @see ArgbValue
 * @see RgbaValue
 */
public class RgbValue extends ArgbValue {
    /**
     * Create an instance representing a color value
     *
     * @param red   value of the red component (0 to 255)
     * @param green value of the green component (0 to 255)
     * @param blue  value of the blue component (0 to 255)
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     */
    public RgbValue(int red, int green, int blue) throws QrConfigurationException {
        super(0xFF, red, green, blue);
    }

    public RgbValue(int rawRgbValue) throws QrConfigurationException {
        super(convertToRawArgbValue(rawRgbValue));
    }

    private static int convertToRawArgbValue(int rawRgbValue) throws QrConfigurationException {
        if ((rawRgbValue & 0xff000000) != 0)
            throw new QrConfigurationException("Given value is too big for a raw RGB value");

        return (0xff << 24) | rawRgbValue;
    }
}
