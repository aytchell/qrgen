package com.github.aytchell.qrgen.colors;

import com.github.aytchell.qrgen.QrConfigurationException;

public class HslaValue extends QrColor {
    private final int hue;
    private final int saturation;
    private final int lightness;

    /**
     * Color value based on the Hue/Saturation/Lightness (HSL) model with an extra alpha channel
     * <p>
     * Not that there are two similar constructors which only differ in the type and valid range of the alpha value.
     *
     * @param hue        hue value in the range 0 <= hue <= 360
     * @param saturation saturation in percent in the range 0 <= saturation <= 100
     * @param lightness  lightness in percent in the range 0 <= lightness <= 100
     * @param alpha      transparency of this color 0 (completely transparent) <= alpha <= 255 (perfectly opaque)
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     * @see HslaValue(int, int, int, double)
     */
    public HslaValue(int hue, int saturation, int lightness, int alpha) throws QrConfigurationException {
        super(convertHslaToRgba(hue, saturation, lightness, alpha));

        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    /**
     * Color value based on the Hue/Saturation/Lightness (HSL) model with an extra alpha channel
     * <p>
     * Not that there are two similar constructors which only differ in the type and valid range of the alpha value.
     *
     * @param hue        hue value in the range 0 <= hue <= 360
     * @param saturation saturation in percent in the range 0 <= saturation <= 100
     * @param lightness  lightness in percent in the range 0 <= lightness <= 100
     * @param alpha      transparency of this color 0.0 (completely transparent) <= alpha <= 1.0 (perfectly opaque)
     * @throws QrConfigurationException is thrown in case one of the values is out of bounds
     * @see HslaValue(int, int, int, int)
     */
    public HslaValue(int hue, int saturation, int lightness, double alpha) throws QrConfigurationException {
        this(hue, saturation, lightness, (int) Math.round(alpha * 255.0));
    }

    private static int convertHslaToRgba(int hue, int saturation, int lightness, int alpha)
            throws QrConfigurationException {
        if ((hue < 0) || (hue > 360) ||
                (saturation < 0) || (saturation > 100) ||
                (lightness < 0) || (lightness > 100) ||
                (alpha < 0) || (alpha > 255)) {
            throw new QrConfigurationException(
                    "HSLA parameters are out of bounds which are (0, 0, 0, 0) to (360, 100, 100, 255)");
        }

        final double chroma = (1.0 - Math.abs(2 * ((double) lightness / 100.0) - 1.0)) * ((double) saturation / 100.0);
        final double hueDash = (double) hue / 60.0;
        final double x = chroma * (1 - Math.abs((hueDash % 2) - 1));
        final double m = ((double) lightness / 100.0) - (chroma / 2.0);

        final RgbTriple rgb = findRgbForHslDependingOnSector(chroma, x, hueDash);
        return QrColor.computeRawArgbVale(
                alpha,
                (int) Math.round((rgb.red + m) * 255.0),
                (int) Math.round((rgb.green + m) * 255.0),
                (int) Math.round((rgb.blue + m) * 255.0)
        );
    }

    private static RgbTriple findRgbForHslDependingOnSector(double chroma, double x, double hueDash)
            throws QrConfigurationException {
        if (hueDash < 0) throw new QrConfigurationException("Failed to convert hsla to rgba");
        if (hueDash <= 1.0) return new RgbTriple(chroma, x, 0.0);
        if (hueDash <= 2.0) return new RgbTriple(x, chroma, 0.0);
        if (hueDash <= 3.0) return new RgbTriple(0.0, chroma, x);
        if (hueDash <= 4.0) return new RgbTriple(0.0, x, chroma);
        if (hueDash <= 5.0) return new RgbTriple(x, 0.0, chroma);
        if (hueDash <= 6.0) return new RgbTriple(chroma, 0.0, x);
        throw new QrConfigurationException("Failed to convert hsla to rgba");
    }

    @Override
    public String toString() {
        return String.format("(%d°, %d%%, %d%%, %d)", hue, saturation, lightness, getAlpha());
    }

    private static class RgbTriple {
        final double red;
        final double green;
        final double blue;

        public RgbTriple(double red, double green, double blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
    }
}
