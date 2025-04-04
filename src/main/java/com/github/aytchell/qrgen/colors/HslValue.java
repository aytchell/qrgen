package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.exceptions.QrConfigurationException;

/**
 * Class to encode color values via "hue saturation lightness"
 * <p>
 * Basically an {@link HslaValue} with an alpha channel set to "opaque"
 *
 * @see HslaValue
 */
public class HslValue extends HslaValue {
    /**
     * Color value based on the Hue/Saturation/Lightness (HSL) model
     *
     * @param hue        hue value in the range 0 &lt;= hue &lt;= 360
     * @param saturation saturation in percent in the range 0 &lt;= saturation &lt;= 100
     * @param lightness  lightness in percent in the range 0 &lt;= lightness &lt;= 100
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     */
    public HslValue(int hue, int saturation, int lightness) throws QrConfigurationException {
        super(hue, saturation, lightness, 0xff);
    }
}
