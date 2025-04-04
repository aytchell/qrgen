package com.github.aytchell.qrgen.config;

import com.github.aytchell.qrgen.colors.QrColor;

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

    public QrColor getOnColor() {
        return onColor;
    }

    public QrColor getOffColor() {
        return offColor;
    }

    public QrColor getOuterMarkerColor() {
        return outerMarkerColor;
    }

    public QrColor getInnerMarkerColor() {
        return innerMarkerColor;
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
