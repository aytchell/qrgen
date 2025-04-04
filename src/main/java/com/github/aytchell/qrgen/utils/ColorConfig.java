package com.github.aytchell.qrgen.utils;

import com.github.aytchell.qrgen.colors.QrColor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorConfig {
    private final QrColor onColor;
    private final QrColor offColor;
    private final QrColor outerMarkerColor;
    private final QrColor innerMarkerColor;

    public ColorConfig(QrColor onColor, QrColor offColor) {
        this(onColor, offColor, onColor);
    }

    public ColorConfig(QrColor onColor, QrColor offColor, QrColor markerColor) {
        this(onColor, offColor, markerColor, markerColor);
    }

    public ColorConfig(QrColor onColor, QrColor offColor, QrColor outerMarkerColor, QrColor innerMarkerColor) {
        this.onColor = onColor;
        this.offColor = offColor;
        this.outerMarkerColor = outerMarkerColor;
        this.innerMarkerColor = innerMarkerColor;
    }

    public Color getOnColorForAwt() {
        return onColor.asAwtColor();
    }

    public Color getOffColorForAwt() {
        return offColor.asAwtColor();
    }

    public Color getOuterMarkerColorForAwt() {
        return outerMarkerColor.asAwtColor();
    }

    public Color getInnerMarkerColorForAwt() {
        return innerMarkerColor.asAwtColor();
    }

    public int determineImageType() {
        if (onColor.hasAlpha() || offColor.hasAlpha() || outerMarkerColor.hasAlpha() || innerMarkerColor.hasAlpha()) {
            return BufferedImage.TYPE_INT_ARGB;
        }
        return BufferedImage.TYPE_INT_RGB;
    }

    public ColorConfig withoutAlpha() {
        return new ColorConfig(
                onColor.withoutAlpha(),
                offColor.withoutAlpha(),
                outerMarkerColor.withoutAlpha(),
                innerMarkerColor.withoutAlpha()
        );
    }
}
