package com.github.aytchell.qrgen;

/**
 * Class to encode color values
 *
 * Basically an {@link ArgbValue} with an alpha channel set to "opaque"
 * @see ArgbValue
 */
public class RgbValue extends ArgbValue {
    /**
     * Create an instance representing a color value
     * @param red value of the red component (0 to 255)
     * @param green value of the green component (0 to 255)
     * @param blue value of the blue component (0 to 255)
     */
    public RgbValue(int red, int green, int blue) {
        super(0xFF, red, green, blue);
    }
}
